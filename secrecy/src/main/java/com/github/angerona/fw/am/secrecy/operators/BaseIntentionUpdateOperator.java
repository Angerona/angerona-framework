package com.github.angerona.fw.am.secrecy.operators;

import com.github.angerona.fw.Agent;
import com.github.angerona.fw.PlanComponent;
import com.github.angerona.fw.PlanElement;
import com.github.angerona.fw.am.secrecy.operators.parameter.PlanParameter;
import com.github.angerona.fw.operators.Operator;
import com.github.angerona.fw.util.Pair;

/**
 * The base class for intention-update operations, subclasses that implement
 * this operator are responsible to select the next {@link PlanElement} that
 * is atomic and can be performed by the agent.
 * 
 * This operation might work on the {@link PlanComponent} to read the current
 * plans of the agent. It also might invoke implementations of {@link BaseSubgoalGenerationOperator}
 * to generate parts of the plan.
 * 
 * The operator returns a {@link PlanElement} that represents the selected intention
 * of the agent.
 * 
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
