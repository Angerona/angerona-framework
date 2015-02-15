package com.github.angerona.fw.motivation.operators.temp;

import java.util.Set;

import com.github.angerona.fw.Agent;
import com.github.angerona.fw.Desire;
import com.github.angerona.fw.operators.Operator;
import com.github.angerona.fw.util.Pair;

/**
 * 
 * @author Manuel Barbi
 *
 */
public class FilterOperator extends Operator<Agent, FilterParameter, Void> {

	public static final String OPERATION_TYPE = "Filter";

	@Override
	public Pair<String, Class<?>> getOperationType() {
		return new Pair<String, Class<?>>(OPERATION_TYPE, FilterOperator.class);
	}

	@Override
	protected FilterParameter getEmptyParameter() {
		return new FilterParameter();
	}

	@Override
	protected Void processImpl(FilterParameter param) {
		// TODO insert motivation buffer

		Set<Desire> desires = param.getDesires().getDesires();
		Intentions intentions = param.getIntention();

		if (!desires.isEmpty()) {
			intentions.setSelected(desires.iterator().next());
		}

		return null;
	}

	@Override
	protected Void defaultReturnValue() {
		return null;
	}

}
