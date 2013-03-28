package angerona.fw.DefendingAgent;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.tweety.logics.conditionallogic.syntax.Conditional;
import net.sf.tweety.logics.firstorderlogic.syntax.Atom;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import net.sf.tweety.logics.firstorderlogic.syntax.Predicate;
import net.sf.tweety.logics.propositionallogic.PlBeliefSet;
import net.sf.tweety.logics.propositionallogic.syntax.Contradiction;
import net.sf.tweety.logics.propositionallogic.syntax.Disjunction;
import net.sf.tweety.logics.propositionallogic.syntax.Negation;
import net.sf.tweety.logics.propositionallogic.syntax.Proposition;
import net.sf.tweety.logics.propositionallogic.syntax.PropositionalFormula;
import net.sf.tweety.logics.propositionallogic.syntax.Tautology;
import angerona.fw.BaseAgentComponent;
import angerona.fw.BaseBeliefbase;
import angerona.fw.DefendingAgent.View;
import angerona.fw.DefendingAgent.Prover.Prover;
import angerona.fw.logic.AnswerValue;
import angerona.fw.logic.conditional.ConditionalBeliefbase;

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
public class CensorComponent extends BaseAgentComponent {
	/** reference to the logback instance used for logging */
	private static Logger LOG = LoggerFactory
			.getLogger(CensorComponent.class);
	
	private Prover.Solver solver = Prover.Solver.FREE_RATIONAL;
	
	public CensorComponent() {
		super();
	}
	
	public static void main(String[] args) {
		CensorComponent cexec = new CensorComponent();
		
		// Logistics example - test klm solver
		PlBeliefSet beliefs = new PlBeliefSet();
		beliefs.add(new Proposition("d11"));
		beliefs.add(new Proposition("s21"));
		beliefs.add(new Proposition("i11_22"));
		
		
		// Build View
		View v = new View(beliefs);
		
		FolFormula d11 = new Atom(new Predicate("d11"));
		FolFormula s21 = new Atom(new Predicate("s21"));
		FolFormula not_s21 = new net.sf.tweety.logics.firstorderlogic.syntax.Negation(s21);
		FolFormula r = new Atom(new Predicate("r"));
		FolFormula s21_r = new net.sf.tweety.logics.firstorderlogic.syntax.Disjunction(not_s21, r); 
		
		v.RefineViewByQuery(d11, AnswerValue.AV_UNKNOWN);
		v.RefineViewByQuery(s21_r, AnswerValue.AV_TRUE);
		v.RefineViewByRevision(s21, AnswerValue.AV_TRUE);
		
		String[] CL_V = cexec.makeBeliefBase(v);
		PropositionalFormula plprove = new Disjunction(new Negation(new Tautology()), new Contradiction());
		
		Prover prover = new Prover();
		System.out.println("Prover: "+prover.prove(CL_V, cexec.translate(plprove), cexec.solver));
	}
	
	public CensorComponent(Prover.Solver solver) {
		super();
		this.solver = solver; 
	}	
	
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
		result.replaceAll("\\+", " true ");
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

	@Override
	public void init(Map<String, String> additionalData) {
		if(additionalData.containsKey("KLMSemantics")) {
			String str = additionalData.get("KLMSemantics");
			try {
				solver = Prover.Solver.valueOf(str.trim());
			} catch(Exception e) {
				LOG.error("Illegal argument for additionalData field 'KLMSemantics' in simulation description: " + str + ". Using default solver: RATIONAL.");
				
			}
		}
	}
	
	@Override
	public Object clone() {
		return new CensorComponent(this.solver);
	}
}
