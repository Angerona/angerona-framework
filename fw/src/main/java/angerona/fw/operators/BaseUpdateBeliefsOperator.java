package angerona.fw.operators;

import angerona.fw.Agent;
import angerona.fw.logic.Beliefs;
import angerona.fw.operators.parameter.EvaluateParameter;
import angerona.fw.util.Pair;

/**
 * Base class for Update Beliefs process of the Agent.
 * @author Tim Janus
 */
public abstract class BaseUpdateBeliefsOperator extends 
	Operator<Agent, EvaluateParameter, Beliefs> {

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

}
