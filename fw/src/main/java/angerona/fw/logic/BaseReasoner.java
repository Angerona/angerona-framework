package angerona.fw.logic;

import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import angerona.fw.BaseBeliefbase;
import angerona.fw.operators.Operator;
import angerona.fw.operators.parameter.ReasonerParameter;
import angerona.fw.util.Pair;

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
	extends Operator<BaseBeliefbase, ReasonerParameter, Pair<Set<FolFormula>, AngeronaAnswer>>{
	
	public static final String OPERATION_TYPE = "BeliefOperator";
	
	/** Default Ctor */
	public BaseReasoner() {}
	
	@Override
	protected ReasonerParameter getEmptyParameter() {
		return new ReasonerParameter();
	}

	@Override
	protected Pair<Set<FolFormula>, AngeronaAnswer> defaultReturnValue() {
		return new Pair<Set<FolFormula>, AngeronaAnswer>(new HashSet<FolFormula>(), null);
	}
	
	@Override 
	protected Pair<Set<FolFormula>, AngeronaAnswer> processInternal(ReasonerParameter params) {
		if(params.getQuery() == null) {
			return new Pair<>(inferInt(params), null);
		} else {
			return queryInt(params);
		}
	}
	
	/** 
	 * infers all the knowledge of the beliefbase and saves it in FolFormula (only Atom and Negation)
	 * @return	A set of FolFormulas representing Cn(Bel).
	 */
	protected abstract Set<FolFormula> inferInt(ReasonerParameter params);
	
	/**
	 * Infers all the knowledge in a given belief base and returns it as a set of FOL formulas.
	 * This helper method maintains the operator callstack and calls inferInt to get the work done.
	 * @return	a set of FOL formulas representing Cn(belief base).
	 */
	public Set<FolFormula> infer(BaseBeliefbase caller) {
		ReasonerParameter params = new ReasonerParameter(caller, null);
		caller.pushOperator(this);
		Set<FolFormula> reval = processInternal(params).first;
		caller.popOperator();
		return reval;
	}
	
	/**
	 * This method determines the answer of the given query
	 * wrt. using the last used beliefbase (if beliefbase is not set null is returned)
	 * @param query a query.
	 * @return the answer to the query.
	 */
	protected abstract  Pair<Set<FolFormula>, AngeronaAnswer> queryInt(ReasonerParameter params);
	
	/**
	 * Querys for the formula given as parameter. Its a helper method which 
	 * maintains the state of the operator callstack and calls the queryInt 
	 * method to get the work done.
	 * @param query
	 * @return An AngeronaAnswer which represents the answer to the query
	 */
	public Pair<Set<FolFormula>, AngeronaAnswer> query(BaseBeliefbase caller, FolFormula query) {
		ReasonerParameter params = new ReasonerParameter(caller, query);
		caller.pushOperator(this);
		Pair<Set<FolFormula>, AngeronaAnswer> reval = processInternal(params);
		caller.popOperator();
		return reval;
	}
	
	@Override
	public Pair<String, Class<?>> getOperationType() {
		Pair<String, Class<?>> reval = new Pair<>();
		reval.first = OPERATION_TYPE;
		reval.second = BaseReasoner.class;
		return reval;
	}
	
	/**
	 * @return the class definition of the belief base this reasoner supports.
	 */
	public abstract Class<? extends BaseBeliefbase> getSupportedBeliefbase();
}
