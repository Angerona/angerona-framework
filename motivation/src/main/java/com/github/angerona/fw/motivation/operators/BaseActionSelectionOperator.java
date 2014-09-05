package com.github.angerona.fw.motivation.operators;

import com.github.angerona.fw.Agent;
import com.github.angerona.fw.operators.Operator;
import com.github.angerona.fw.util.Pair;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public abstract class BaseActionSelectionOperator extends Operator<Agent, ActionSelectionParameter, Void> {

	public static final String OPERATION_TYPE = "ActionSelection";

	@Override
	public Pair<String, Class<?>> getOperationType() {
		return new Pair<String, Class<?>>(OPERATION_TYPE, BaseActionSelectionOperator.class);
	}

	@Override
	protected ActionSelectionParameter getEmptyParameter() {
		return new ActionSelectionParameter();
	}

	@Override
	protected Void defaultReturnValue() {
		return null;
	}

}
