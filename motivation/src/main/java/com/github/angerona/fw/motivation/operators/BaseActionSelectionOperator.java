package com.github.angerona.fw.motivation.operators;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.angerona.fw.Agent;
import com.github.angerona.fw.operators.Operator;
import com.github.angerona.fw.util.Pair;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public abstract class BaseActionSelectionOperator extends Operator<Agent, ActionSelectionParameter, Void> {

	private static final Logger LOG = LoggerFactory.getLogger(BaseActionSelectionOperator.class);

	public static final String OPERATION_TYPE = "ActionSelection";

	public BaseActionSelectionOperator() {
		LOG.debug("created {}", this.getClass().getSimpleName());
	}

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
