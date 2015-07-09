package com.github.angerona.fw.motivation.dao.impl;

import java.util.List;

import net.sf.tweety.logics.fol.syntax.FolFormula;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.angerona.fw.Action;
import com.github.angerona.fw.Desire;
import com.github.angerona.fw.logic.AnswerValue;
import com.github.angerona.fw.logic.Beliefs;
import com.github.angerona.fw.logic.Desires;
import com.github.angerona.fw.motivation.dao.ActionSequenceDao;
import com.github.angerona.fw.motivation.dao.BeliefState;
import com.github.angerona.fw.motivation.dao.TimeSlotDao;

/**
 * 
 * @author Manuel Barbi
 *
 */
public class BeliefStateImpl implements BeliefState {

	private static final Logger LOG = LoggerFactory.getLogger(BeliefStateImpl.class);

	private Beliefs beliefs;
	private ActionSequenceDao sequences;
	private TimeSlotDao timeSlots;
	private Desires desires;

	public BeliefStateImpl(Beliefs beliefs, ActionSequenceDao sequences, TimeSlotDao timeSlots, Desires desires) {
		if (beliefs == null) {
			throw new NullPointerException("beliefs must not be null");
		}

		this.beliefs = beliefs;
		this.sequences = sequences;
		this.timeSlots = timeSlots;
		this.desires = desires;
	}

	@Override
	public boolean isReliable(Desire d) {

		// check whether sequence-component is initialized
		if (sequences == null) {
			LOG.warn("sequence-component is not initialized");
			return true;
		}

		List<Action> sequence = sequences.getSequence(d);

		// check weather any sequence exists
		if (sequence == null) {
			return false;
		}

		// check whether time-slot-component is initialized
		if (timeSlots == null) {
			LOG.warn("time-slot-component is not initialized");
			return true;
		}

		Integer minSlot = timeSlots.getMinSlot();

		// check whether shortest sequence is smaller than minimal time-slot
		if (minSlot != null) {
			return sequence.size() < minSlot;
		}

		return true;
	}

	@Override
	public boolean isSatisfied(Desire d) {
		if (desires != null) {
			return !desires.getDesires().contains(d);
		}

		return false;
	}

	@Override
	public boolean verify(FolFormula statement) {
		// check weather statement is true
		if (statement != null) {
			return beliefs.getWorldKnowledge().reason(statement).getAnswerValue() == AnswerValue.AV_TRUE;
		}

		return true;
	}

}
