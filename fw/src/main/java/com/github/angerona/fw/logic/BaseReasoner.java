package com.github.angerona.fw.logic;

import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.logics.fol.syntax.FolFormula;

import com.github.angerona.fw.BaseBeliefbase;
import com.github.angerona.fw.operators.Operator;
import com.github.angerona.fw.operators.parameter.ReasonerParameter;
import com.github.angerona.fw.util.Pair;

/**
 * Base class for all reasoners used by the Angerona project.
 * 
 * To query the reasoner a subset of the FOL Language defined in Tweety is used.
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
	 * Infers all the knowledge of the BaseBeliefbase and saves it in FolFormula (only Atom and Negation)
	 * @param params	The input parameter containing the BaseBeliefbase.
	 * @return	A set of FolFormula representing Cn(Bel).
	 */
	protected abstract Set<FolFormula> inferInt(ReasonerParameter params);
	
	/**
	 * Infers all the knowledge in a given BaseBeliefbase and returns it as a set of FOLFormula.
	 * This helper method maintains the operator callstack and calls inferInt to get the work done.
	 * @param caller	The BaseBeliefbase which acts as caller.
	 * @return	A set of FolFormula representing Cn(belief base).
	 */
	public Set<FolFormula> infer(BaseBeliefbase caller) {
		ReasonerParameter params = new ReasonerParameter(caller, null);
		caller.getStack().pushOperator(this);
		Set<FolFormula> reval = processInternal(params).first;
		caller.getStack().popOperator();
		return reval;
	}
	
	/**
	 * This method determines the answer of the given Query.
	 * @param params 	A structure containing the BaseBeliefbase and the Query.
	 * @return the answer to the query.
	 */
	protected abstract  Pair<Set<FolFormula>, AngeronaAnswer> queryInt(ReasonerParameter params);
	
	/**
	 * Query for the FolFormula given as parameter. Its a helper method which 
	 * maintains the state of the operator callstack and calls the queryInt 
	 * method to get the work done.
	 * @param caller	The BaseBeliefbase which acts as caller.
	 * @param query		The query.
	 * @return An AngeronaAnswer which represents the answer to the query
	 */
	public Pair<Set<FolFormula>, AngeronaAnswer> query(BaseBeliefbase caller, FolFormula query) {
		ReasonerParameter params = new ReasonerParameter(caller, query);
		caller.getStack().pushOperator(this);
		Pair<Set<FolFormula>, AngeronaAnswer> reval = processInternal(params);
		caller.getStack().popOperator();
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
