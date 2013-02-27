package angerona.fw.DefendingAgent.operators.def;

import java.util.Collection;
import java.util.Set;

import net.sf.tweety.logics.conditionallogic.syntax.Conditional;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import net.sf.tweety.logics.propositionallogic.PlBeliefSet;
import net.sf.tweety.logics.propositionallogic.syntax.PropositionalFormula;
import angerona.fw.Agent;
import angerona.fw.DefendingAgent.View;
import angerona.fw.DefendingAgent.ViewComponent;
import angerona.fw.DefendingAgent.Prover.Prover;
import angerona.fw.DefendingAgent.comm.Revision;
import angerona.fw.comm.Query;

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

	
	// rethink separation of censor/subgoalgenerator. the current cut seems forced and unintuitive
	public boolean processQuery(Agent ag, Query q) {
		// procedure 3
		
//		String[] knowledgeBase = this.makeBeliefBase(ag.getComponent(ViewComponent.class).getView(ag.toString()));
//		Prover p = new Prover();
//		return p.prove(knowledgeBase, translate(q.getQuestion()), Prover.Solver.FREE_RATIONAL);
		return false;
	}
	
	public boolean processRevision(Agent ag, Revision rev) {
		// procedure 4
		
		return false;
	}
	
	public boolean skepticalInference(View view, FolFormula formula) {
		String[] knowledgeBase = this.makeBeliefBase(view);
		Prover p = new Prover();
		return p.prove(knowledgeBase, translate(formula), Prover.Solver.FREE_RATIONAL);
	}
	
	public boolean poss(View view) {
		//TODO write
		// check if an ocf exists, which is compatible to view
		return false;
	}
	
	/**
	 * Create a BelifeBase out of the View on the attacking agent
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
		String[] belifeBase = new String[n];
		for(Conditional a: positiveConditionalBeliefs){
			belifeBase[i] = translate(a.getPremise()) + "=>" + translate(a.getConclusion());
			i++;
		}
		for(Conditional a: negativeConditionalBeliefs){
			belifeBase[i] = "neg ("+ translate(a.getPremise()) + "=>" + translate(a.getConclusion()) +")";
			i++;
		}
		for(PropositionalFormula a: beliefSet){
			belifeBase[i] = "neg (" + translate(a) + "=> false)";
			i++;
		}
		return belifeBase;
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
		String[] b;
		String result = "";
		//change || to or
		b = formula.toString().split("||");
		if(b.length > 1){
			for(int i = 0; i<b.length-1;i++){
				result += b[i] + " or ";
			}
		}
		result += b[b.length-1];
		//change && to and
		b = result.split("&&");
		result = "";
		if(b.length > 1){
			for(int i = 0; i<b.length-1;i++){
			result += b[i] + " and ";
			}
		}
		result += b[b.length-1];
		//change - to neg
		b = result.split("-");
		result = "";
		if(b.length > 1){
			for(int i = 0; i<b.length-1;i++){
			result += b[i] + " neg ";
			}
		}
		result += b[b.length-1];
		
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
		String[] b;
		String result = "";
		//change || to or
		b = formula.toString().split("||");
		if(b.length > 1){
			for(int i = 0; i<b.length-1;i++){
				result += b[i] + " or ";
			}
		}
		result += b[b.length-1];
		//change && to and
		b = result.split("&&");
		result = "";
		if(b.length > 1){
			for(int i = 0; i<b.length-1;i++){
			result += b[i] + " and ";
			}
		}
		result += b[b.length-1];
		//change - to neg
		b = result.split("-");
		result = "";
		if(b.length > 1){
			for(int i = 0; i<b.length-1;i++){
			result += b[i] + " neg ";
			}
		}
		result += b[b.length-1];
		
		return "( "+ result +" )";
	}
}
