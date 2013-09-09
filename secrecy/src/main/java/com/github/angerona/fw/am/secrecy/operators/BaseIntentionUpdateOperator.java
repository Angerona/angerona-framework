package com.github.angerona.fw.am.secrecy.operators;

import com.github.angerona.fw.Agent;
import com.github.angerona.fw.PlanElement;
import com.github.angerona.fw.am.secrecy.operators.parameter.PlanParameter;
import com.github.angerona.fw.operators.Operator;
import com.github.angerona.fw.util.Pair;

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
