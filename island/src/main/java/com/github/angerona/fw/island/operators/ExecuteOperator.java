package com.github.angerona.fw.island.operators;

import com.github.angerona.fw.Agent;
import com.github.angerona.fw.am.secrecy.operators.parameter.PlanParameter;
import com.github.angerona.fw.operators.Operator;
import com.github.angerona.fw.util.Pair;

/**
 * 
 * @author Manuel Barbi
 *
 */
public class ExecuteOperator extends Operator<Agent, PlanParameter, Void> {

	public static final String OPERATION_TYPE = "Execute";

	@Override
	public Pair<String, Class<?>> getOperationType() {
		return new Pair<String, Class<?>>(OPERATION_TYPE, ExecuteOperator.class);
	}

	@Override
	protected PlanParameter getEmptyParameter() {
		return new PlanParameter();
	}

	@Override
	protected Void processImpl(PlanParameter preprocessedParameters) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Void defaultReturnValue() {
		return null;
	}

}
