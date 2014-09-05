package com.github.angerona.fw.motivation.operators;

import com.github.angerona.fw.Agent;
import com.github.angerona.fw.operators.Operator;
import com.github.angerona.fw.util.Pair;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public abstract class BaseFilterOperator extends Operator<Agent, FilterOperatorParameter, Integer> {

	public static final String OPERATION_TYPE = "Filter";

	@Override
	public Pair<String, Class<?>> getOperationType() {
		return new Pair<String, Class<?>>(OPERATION_TYPE, BaseFilterOperator.class);
	}

	@Override
	protected FilterOperatorParameter getEmptyParameter() {
		return new FilterOperatorParameter();
	}

	@Override
	protected Integer defaultReturnValue() {
		return 0;
	}

}
