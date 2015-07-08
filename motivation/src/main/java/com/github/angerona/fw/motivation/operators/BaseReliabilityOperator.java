package com.github.angerona.fw.motivation.operators;

import com.github.angerona.fw.Agent;
import com.github.angerona.fw.am.secrecy.operators.parameter.PlanParameter;
import com.github.angerona.fw.island.operators.parameter.IslandPlanParameter;
import com.github.angerona.fw.operators.Operator;
import com.github.angerona.fw.util.Pair;

/**
 * 
 * @author Manuel Barbi
 *
 */
public abstract class BaseReliabilityOperator extends Operator<Agent, PlanParameter, Void> {

	public static final String OPERATION_TYPE = "Reliability";

	@Override
	public Pair<String, Class<?>> getOperationType() {
		return new Pair<String, Class<?>>(OPERATION_TYPE, BaseReliabilityOperator.class);
	}

	@Override
	protected PlanParameter getEmptyParameter() {
		return new IslandPlanParameter();
	}

	@Override
	protected Void defaultReturnValue() {
		return null;
	}

}
