package com.github.kreatures.core.operators;

import com.github.kreatures.core.Agent;
import com.github.kreatures.core.Perception;
import com.github.kreatures.core.logic.Beliefs;
import com.github.kreatures.core.operators.parameter.EvaluateParameter;
import com.github.kreatures.core.util.Pair;

/**
 * Abstract base class for the update beliefs process of the Agent, sub classes have
 * to inform
 * @author Tim Janus
 */
public abstract class BaseUpdateBeliefsOperator extends 
	Operator<Agent, EvaluateParameter, Beliefs> {

	/** unique name of the operation implemented by subclasses. */
	public static final String OPERATION_NAME = "UpdateBeliefs";
	
	@Override
	public Pair<String, Class<?>> getOperationType() {
		return new Pair<String, Class<?>>(OPERATION_NAME,
				BaseUpdateBeliefsOperator.class);
	}
	
	@Override
	protected EvaluateParameter getEmptyParameter() {
		return new EvaluateParameter();
	}

	@Override
	protected Beliefs defaultReturnValue() {
		return null;
	}
	
	@Override
	protected void prepare(EvaluateParameter params) {
		
	}
	
	/**
	 * This method has to be called by sub classes when a belief base has changed.
	 * It informs the AgentComponent instances of the agent about the update.
	 * @param param			The EvaluateParameter
	 * @param oldBeliefs	The version of the Beliefs before the update.
	 */
	protected void onBeliefbaseUpdate(EvaluateParameter param, Beliefs oldBeliefs) {
		param.getAgent().onUpdateBeliefs((Perception)param.getAtom(), oldBeliefs);
	}
}
