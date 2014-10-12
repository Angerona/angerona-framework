package com.github.angerona.fw.motivation.operators;

import com.github.angerona.fw.Agent;
import com.github.angerona.fw.operators.Operator;
import com.github.angerona.fw.util.Pair;

/**
 * 
 * @author Manuel Barbi
 *
 */
public abstract class BasePlanOperator extends Operator<Agent, PlanOperatorParameter, Void> {

	public static final String OPERATION_TYPE = "PlanCalculation";

	@Override
	public Pair<String, Class<?>> getOperationType() {
		return new Pair<String, Class<?>>(OPERATION_TYPE, BasePlanOperator.class);
	}

	@Override
	protected PlanOperatorParameter getEmptyParameter() {
		return new PlanOperatorParameter();
	}

	@Override
	protected Void defaultReturnValue() {
		return null;
	}

}
