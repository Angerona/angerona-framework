package angerona.fw.DefendingAgent.operators.def;

import java.util.Collection;
import java.util.Set;

import net.sf.tweety.logics.conditionallogic.syntax.Conditional;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import net.sf.tweety.logics.propositionallogic.PlBeliefSet;
import net.sf.tweety.logics.propositionallogic.syntax.Contradiction;
import net.sf.tweety.logics.propositionallogic.syntax.Disjunction;
import net.sf.tweety.logics.propositionallogic.syntax.Negation;
import net.sf.tweety.logics.propositionallogic.syntax.PropositionalFormula;
import net.sf.tweety.logics.propositionallogic.syntax.Tautology;
import angerona.fw.DefendingAgent.View;
import angerona.fw.DefendingAgent.Prover.Prover;

/**
 * Implementation of the censor component of a defending censor agent.
 * Simulates the results of all possible answers to a query/revision request by an attacker.
 * If there is at least one possible answer, which would reveal a secret to the attacker,
 * the request is refused. This approach denies any form of meta-inference. See [1] for details and
 * proofs.
 * 
 * @author Sebastian Homann, Pia Wierzoch
 * @see [1] Biskup, Joachim and Tadros, Cornelia. Revising Belief without Revealing Secrets
 */
public class Censor {
	private Prover.Solver solver = Prover.Solver.FREE_RATIONAL;
	
	/**
	 *  the censor uses the klmlean framework to prove sentences in
	 *  one of five possible inference systems. The default is Rational.
	 */
	public void setSolver(Prover.Solver solver) {
		this.solver = solver;
	}

	/**
	 * Calculate a skeptical inference of a given formula under a specific view.
	 * Returns true iff formula follows skeptically from the view and false otherwise 
	 * @param view
	 * @param formula
	 * @return
	 */
	public boolean skepticalInference(View view, FolFormula formula) {
		String[] knowledgeBase = this.makeBeliefBase(view);
		Prover p = new Prover();
		return p.prove(knowledgeBase, translate(formula), solver);
	}
	
	/**
	 * Check if there exists a consistent consequence relation that is compatible with the given view 
	 * @param view
	 */
	public boolean poss(View view) {
		// there is a possible consequence relation for a view V iff "true -> false" does not
		// follow from CL(V)
		String[] knowledgeBase = this.makeBeliefBase(view);
		PropositionalFormula contradiction = new Disjunction(new Negation(new Tautology()), new Contradiction());
		Prover p = new Prover();
		return ! p.prove(knowledgeBase, translate(contradiction), solver);
	}
	
	/**
	 * Create a BeliefBase out of the View on the attacking agent
	 * @param v
	 * 	View on the attacking agent
	 * @return a String[] that represent the approximation of the knowledge of the attacking agent
	 * @see[1] Biskup, Joachim and Tadros, Cornelia. Revising Belief without Revealing Secrets
	 */
	private String[] makeBeliefBase(View v){
		Set<Conditional> positiveConditionalBeliefs = v.getPositiveConditionalBeliefs();
		Set<Conditional> negativeConditionalBeliefs = v.getNegativeConditionalBeliefs();
		PlBeliefSet beliefSet = v.getBeliefSet();
		int n = 0, i=0;
		n += positiveConditionalBeliefs.size() + negativeConditionalBeliefs.size() + beliefSet.size();
		String[] beliefBase = new String[n];
		for(Conditional a: positiveConditionalBeliefs){
			beliefBase[i] = translate(a.getPremise()) + "=>" + translate(a.getConclusion());
			i++;
		}
		for(Conditional a: negativeConditionalBeliefs){
			beliefBase[i] = "neg ("+ translate(a.getPremise()) + "=>" + translate(a.getConclusion()) +")";
			i++;
		}
		for(PropositionalFormula a: beliefSet){
			beliefBase[i] = "neg (" + translate(a) + "=> false)";
			i++;
		}
		return beliefBase;
	}
	
	/**
	 * Translate a Collection of PropositionalFormulas by using the 
	 * method translate for PropositionalFormulas and connect the Formulas with "and"
	 * @param formulas
	 * 	Collection of PropositionalFormulas to translate
	 * @return a String where the operators are changed to "or" "and" "neg" and the formulas are 
	 * Connected with "and"
	 */
	private String translate(Collection<PropositionalFormula> formulas){
		String result = "";
		for(PropositionalFormula a : formulas){
			if(result.equalsIgnoreCase("")){
				result = translate(a); 
			}else{
				result = result + " and " + translate(a); 
			}
		}
		return result;
	}
	
	/**
	 * Translate PropositionalForumla to a String that the TheoremSolver 
	 * can understand by changing "||" "&&" "-" to "or" "and" "neg"
	 * @param formula
	 * 	the PropositionalFormula to translate
	 * @return a String where the operators are changed to "or" "and" "neg"
	 */
	private String translate(PropositionalFormula formula){
		String result = formula.toString();

		result.replaceAll("\\|\\|", "or");
		result.replaceAll("\\&\\&", " and ");

		// TODO: i'm not sure if the prolog-parser understands true and false ... 
		result.replaceAll("-", " neg ");
		result.replaceAll("+", " true ");
		result.replaceAll("!", " false ");
		
		return "( "+ result +" )";
	}
	
	/**
	 * Translate FolForumla to a String that the TheoremSolver 
	 * can understand by changing "||" "&&" "-" to "or" "and" "neg"
	 * @param formula
	 * 	the FolFormula to translate
	 * @return a String where the operators are changed to "or" "and" "neg"
	 */
	private String translate(FolFormula formula){
		String result = formula.toString();
		//change || to or
		result.replaceAll("\\|\\|", "or");
		result.replaceAll("\\&\\&", " and ");
		result.replaceAll("-", " neg ");
		result.replaceAll("+", " true ");
		result.replaceAll("!", " false ");
		
		return "( "+ result +" )";
	}
}
