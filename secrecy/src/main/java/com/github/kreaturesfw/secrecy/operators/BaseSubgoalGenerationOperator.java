package com.github.kreaturesfw.secrecy.operators;

import com.github.kreaturesfw.core.Agent;
import com.github.kreaturesfw.core.PlanComponent;
import com.github.kreaturesfw.core.operators.Operator;
import com.github.kreaturesfw.core.util.Pair;
import com.github.kreaturesfw.secrecy.operators.parameter.PlanParameter;


/**
 * Base class for all subgoal-generation implementations, that means
 * for planners.
 * 
 * An implementation of the operator might use the {@link PlanComponent} as
 * data storage to store the generated plans.
 * 
 * It returns a boolean indicating if there are any changes to the
 * {@link PlanComponent}, that means if there are new sub-goals in the
 * plan.
 * 
 * @author Tim Janus
 */
public abstract class BaseSubgoalGenerationOperator 
	extends Operator<Agent, PlanParameter, Boolean> {

	public static final String OPERATION_NAME = "SubgoalGeneration";
	
	@Override
	public Pair<String, Class<?>> getOperationType() {
		return new Pair<String, Class<?>>(OPERATION_NAME,
				BaseSubgoalGenerationOperator.class);
	}
	
	@Override
	protected PlanParameter getEmptyParameter() {
		return new PlanParameter();
	}

	@Override
	protected Boolean defaultReturnValue() {
		return false;
	}
	
}
