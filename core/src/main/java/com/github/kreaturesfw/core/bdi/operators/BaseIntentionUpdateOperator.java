package com.github.kreaturesfw.core.bdi.operators;

import com.github.kreaturesfw.core.basic.Agent;
import com.github.kreaturesfw.core.basic.BaseOperator;
import com.github.kreaturesfw.core.bdi.PlanElement;
import com.github.kreaturesfw.core.bdi.components.PlanComponent;
import com.github.kreaturesfw.core.bdi.operators.parameter.PlanParameter;
import com.github.kreaturesfw.core.util.Pair;

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
	BaseOperator<Agent, PlanParameter, PlanElement> {

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
