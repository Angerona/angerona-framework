package com.github.kreaturesfw.core.logic;

import java.util.HashSet;
import java.util.Set;

import com.github.kreaturesfw.core.basic.BaseOperator;
import com.github.kreaturesfw.core.bdi.components.BaseBeliefbase;
import com.github.kreaturesfw.core.operators.parameter.ReasonerParameter;
import com.github.kreaturesfw.core.util.Pair;

import net.sf.tweety.logics.fol.syntax.FolFormula;

/**
 * Base class for all reasoners used in the Angerona framework, sub classes can implement
 * a reasoning mechanism for a specific knowledge representation mechanism, such that
 * this reasoning mechanism can be used by a sub-class of {@link BaseBeliefbase}.
 * 
 * To query the reasoner a subset of the FOL Language defined in Tweety is used.
 * It is the responsibility of the Reasoner implementation to translate the given
 * query into its native language. 
 * 
 * Sub classes have to implement the method with the ending *Impl to provide their
 * own implementation. They must not override the infer() or query() method because
 * those do internal work and then call their *Impl versions. The processImpl() method
 * contains a appropriate default behavior such that it is highly recommended to not
 * override this method unless you know what you are doing.
 * 
 * @author Tim Janus
 */
public abstract class BaseReasoner 
	extends BaseOperator<BaseBeliefbase, ReasonerParameter, Pair<Set<FolFormula>, AngeronaAnswer>>{
	
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
	protected Pair<Set<FolFormula>, AngeronaAnswer> processImpl(ReasonerParameter params) {
		if(params.getQuery() == null) {
			return new Pair<>(inferImpl(params), null);
		} else {
			return queryImpl(params);
		}
	}
	
	/** 
	 * Infers all the knowledge of the BaseBeliefbase and saves it in FolFormula (only Atom and Negation)
	 * @param params	The input parameter containing the BaseBeliefbase.
	 * @return	A set of FolFormula representing the literals in Cn(Bel).
	 */
	protected abstract Set<FolFormula> inferImpl(ReasonerParameter params);
	
	/**
	 * Infers all the knowledge in a given BaseBeliefbase and returns it as a set of FOLFormula.
	 * This helper method maintains the operator callstack and calls inferInt to get the work done.
	 * @param caller	The BaseBeliefbase which acts as caller.
	 * @return	A set of FolFormula representing Cn(belief base).
	 */
	public Set<FolFormula> infer(BaseBeliefbase caller) {
		ReasonerParameter params = new ReasonerParameter(caller, null);
		caller.getStack().pushOperator(this);
		Set<FolFormula> reval = processImpl(params).first;
		caller.getStack().popOperator();
		return reval;
	}
	
	/**
	 * This method determines the answer of the given Query.
	 * @param params 	A structure containing the BaseBeliefbase and the Query.
	 * @return the answer to the query.
	 */
	protected abstract  Pair<Set<FolFormula>, AngeronaAnswer> queryImpl(ReasonerParameter params);
	
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
		Pair<Set<FolFormula>, AngeronaAnswer> reval = processImpl(params);
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
