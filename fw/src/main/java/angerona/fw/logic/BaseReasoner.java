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
 * To query the reasoner a subset of the FOL Language defined in tweety is used.
 * It is the responsibility of the Reasoner implementation to translate the given
 * query into its native language. 
 * 
 * @author Tim Janus
 */
public abstract class BaseReasoner 
	extends Operator<ReasonerParameter, AngeronaAnswer>{
	
	/** 
	 * TODO: There are thoughts to share the operator instances between callers
	 * but the reasoner is the only operator which has implemented something like
	 * this so far. The task has no high priority.
	 */
	protected BaseBeliefbase actualBeliefbase;
	
	/** Default Ctor */
	public BaseReasoner() {}
	
	@Override
	protected ReasonerParameter getEmptyParameter() {
		return new ReasonerParameter();
	}

	@Override
	protected AngeronaAnswer defaultReturnValue() {
		return null;
	}
	
	/** 
	 * infers all the knowledge of the beliefbase and saves it in FolFormula (only Atom and Negation)
	 * @return	A set of FolFormulas representing Cn(Bel).
	 */
	protected abstract Set<FolFormula> inferInt();
	
	/**
	 * Infers all the knowledge in a given belief base and returns it as a set of FOL formulas.
	 * This helper method maintains the operator callstack and calls inferInt to get the work done.
	 * @return	a set of FOL formulas representing Cn(belief base).
	 */
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
	 * @return	An AngeronaAnswer containing the answer to the given question.
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
	
	/**
	 * Querys for the formula given as parameter. Its a helper method which 
	 * maintains the state of the operator callstack and calls the queryInt 
	 * method to get the work done.
	 * @param query
	 * @return An AngeronaAnswer which represents the answer to the query
	 */
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
}
