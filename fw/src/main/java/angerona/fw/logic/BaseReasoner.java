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
	protected abstract Set<FolFormula> inferInt();
	
	public Set<FolFormula> infer() {
		getOwner().pushOperator(this);
		Set<FolFormula> reval = inferInt();
		getOwner().popOperator();
		return reval;
	}
	
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
	protected abstract Answer queryInt(FolFormula query);
	
	public Answer query(FolFormula query) {
		getOwner().pushOperator(this);
		Answer reval = queryInt(query);
		getOwner().popOperator();
		return reval;
	}
	
	/**
	 * @return the class definition of the belief base this reasoner supports.
	 */
	public abstract Class<? extends BaseBeliefbase> getSupportedBeliefbase();

	//This ought to return a Set rather than a LinkedList. Currently a LinkedList for testing purposes (ordering is easier)
	public Set<AngeronaDetailAnswer> queryForAllAnswers(BaseBeliefbase bb, FolFormula question) {
		// It's not good that such a specific method for AspDetailReasoners is in here...
		actualBeliefbase = bb;
		return queryForAllAnswers(question);	
	}
	public Set<AngeronaDetailAnswer> queryForAllAnswers(FolFormula query) {
		// It's not good that such a specific method for AspDetailReasoners is in here...
		return null;
	}
}
