package angerona.fw.operators;

import angerona.fw.PlanElement;
import angerona.fw.operators.parameter.PlanParameter;

/**
 * The base class for all filter implementations.
 * @author Tim Janus
 */
public abstract class BaseIntentionUpdateOperator extends 
	Operator<PlanParameter, PlanElement> {

	@Override
	protected PlanParameter getEmptyParameter() {
		return new PlanParameter();
	}

	@Override
	protected PlanElement defaultReturnValue() {
		return null;
	}
	
}
