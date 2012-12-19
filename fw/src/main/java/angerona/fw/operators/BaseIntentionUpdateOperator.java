package angerona.fw.operators;

import angerona.fw.Agent;
import angerona.fw.PlanElement;
import angerona.fw.operators.parameter.PlanParameter;
import angerona.fw.util.Pair;

/**
 * The base class for all filter implementations.
 * @author Tim Janus
 */
public abstract class BaseIntentionUpdateOperator extends 
	Operator<Agent, PlanParameter, PlanElement> {

	public static final String OPERATION_NAME = "IntentionUpdate";
	
	@Override
	public Pair<String, Class<?>> getOperationType() {
		return new Pair<String, Class<?>>(OPERATION_NAME,
				BaseIntentionUpdateOperator.class);
	}
	
	@Override
	protected PlanParameter getEmptyParameter() {
		return new PlanParameter();
	}

	@Override
	protected PlanElement defaultReturnValue() {
		return null;
	}
	
}
