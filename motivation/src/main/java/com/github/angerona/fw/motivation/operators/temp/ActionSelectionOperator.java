package com.github.angerona.fw.motivation.operators.temp;

import com.github.angerona.fw.Agent;
import com.github.angerona.fw.Intention;
import com.github.angerona.fw.motivation.ActionSequence;
import com.github.angerona.fw.motivation.reliable.ActionSequenceDao;
import com.github.angerona.fw.operators.Operator;
import com.github.angerona.fw.util.Pair;

/**
 * 
 * @author Manuel Barbi
 *
 */
public class ActionSelectionOperator extends Operator<Agent, ActionSelectionParameter, Void> {

	public static final String OPERATION_TYPE = "ActionSelection";

	@Override
	public Pair<String, Class<?>> getOperationType() {
		return new Pair<String, Class<?>>(OPERATION_TYPE, ActionSelectionOperator.class);
	}

	@Override
	protected ActionSelectionParameter getEmptyParameter() {
		return new ActionSelectionParameter();
	}

	@Override
	protected Void processImpl(ActionSelectionParameter param) {
		Intentions intentions = param.getIntentions();
		ActionSequenceDao sequences = param.getSequences();

		Intention selected = intentions.getSelected();

		if (selected != null && selected instanceof DIntention) {
			ActionSequence seq = sequences.getSequence(((DIntention) selected).getRelated());

			if (seq != null) {
				if (seq.getLength() > 0) {
					param.getAgent().getEnvironment().sendAction(seq.iterator().next());
				} else {
					param.getAgent().report("agent decided to wait");
				}
			} else {
				param.getAgent().report("no action available");
			}
		} else {
			param.getAgent().report("no intention available");
		}

		return null;
	}

	@Override
	protected Void defaultReturnValue() {
		return null;
	}

}
