package com.github.angerona.fw.motivation.operators;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import com.github.angerona.fw.Agent;
import com.github.angerona.fw.Desire;
import com.github.angerona.fw.motivation.ActionSequence;
import com.github.angerona.fw.motivation.reliable.ActionSequenceDao;
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
	protected Void processImpl(PlanOperatorParameter param) {
		Collection<Desire> desires = param.getDesires();
		ActionSequenceDao sequences = param.getSequences();
		sequences.clear();

		ActionSequence seq;

		for (Desire d : desires) {
			seq = getMinSequence(d);

			if (seq != null) {
				sequences.putSequence(d, seq);
			}
		}

		return null;
	}

	protected ActionSequence getMinSequence(Desire d) {
		Set<ActionSequence> sequences = getSequences(d);

		if (sequences != null && !sequences.isEmpty()) {
			return Collections.min(sequences);
		}

		return null;
	}

	protected abstract Set<ActionSequence> getSequences(Desire d);

	@Override
	protected PlanOperatorParameter getEmptyParameter() {
		return new PlanOperatorParameter();
	}

	@Override
	protected Void defaultReturnValue() {
		return null;
	}

}
