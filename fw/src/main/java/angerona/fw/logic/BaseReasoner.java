package angerona.fw.logic;

import java.util.Set;

import net.sf.tweety.Answer;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import angerona.fw.BaseBeliefbase;
import angerona.fw.operators.Operator;
import angerona.fw.operators.parameter.ReasonerParameter;

/**
 * Base class for all reasoner used by the Angerona project.
 * 
 *
 * To query the reasoner a subset of the FOL Language defined in tweety is used.
 * It is the responsibility of the Reasoner implementation to translate the given
 * query into its native language. 
 * 
 * @author Tim Janus
 */
public abstract class BaseReasoner 
	extends Operator<ReasonerParameter, AngeronaAnswer>{
	
	protected BaseBeliefbase actualBeliefbase;
	
	public BaseReasoner() {
	}
	
	/** 
	 * infers all the knowledge of the beliefbase and saves it in FolFormula (only Atom and Negation)
	 * @return	A set of FolFormulas representing Cn(Bel).
	 */
	public abstract Set<FolFormula> infer();
	
	/**
	 * infers all the knowledge of the beliefbase and saves it in FolFormula (only Atom and Negation)
	 * @param bb
	 * @return A set of FolFormulas representing Cn(Bel).
	 */
	public Set<FolFormula> infer(BaseBeliefbase bb) {
		actualBeliefbase = bb;
		return infer();
	}
	
	/**
	 * queries for question in the given beliefbase bb
	 * @param bb
	 * @param question
	 * @return
	 */
	public Answer query(BaseBeliefbase bb, FolFormula question) {
		actualBeliefbase = bb;
		return query(question);
	}
	
	/**
	 * This method determines the answer of the given query
	 * wrt. using the last used beliefbase (if beliefbase is not set null is returned)
	 * @param query a query.
	 * @return the answer to the query.
	 */
	protected abstract Answer query(FolFormula query);
	
	
	/**
	 * @return the class definition of the belief base this reasoner supports.
	 */
	public abstract Class<? extends BaseBeliefbase> getSupportedBeliefbase();
}
