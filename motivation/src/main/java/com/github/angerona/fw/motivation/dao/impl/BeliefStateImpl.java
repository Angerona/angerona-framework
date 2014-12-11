package com.github.angerona.fw.motivation.dao.impl;

import net.sf.tweety.logics.fol.syntax.FolFormula;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.angerona.fw.Desire;
import com.github.angerona.fw.logic.AnswerValue;
import com.github.angerona.fw.logic.Beliefs;
import com.github.angerona.fw.motivation.ActionSequence;
import com.github.angerona.fw.motivation.dao.BeliefState;
import com.github.angerona.fw.motivation.reliable.ActionSequenceDao;
import com.github.angerona.fw.motivation.reliable.TimeSlotDao;

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

	public BeliefStateImpl(Beliefs beliefs, ActionSequenceDao actionSequenceDao, TimeSlotDao timeSlotDao) {
		if (beliefs == null) {
			throw new NullPointerException("beliefs must not be null");
		}

		this.beliefs = beliefs;
		this.sequences = actionSequenceDao;
		this.timeSlots = timeSlotDao;
	}

	@Override
	public boolean isReliable(Desire d) {

		// check whether sequence-component is initialized
		if (sequences == null) {
			LOG.warn("sequence-component is not initialized");
			return true;
		}

		ActionSequence sequence = sequences.getSequence(d);

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
			return sequence.getLength() < minSlot;
		}

		return true;
	}

	@Override
	public boolean isSatisfied(Desire d) {
		// check weather negation of desire formula is true
		if (d != null && d.getFormula() != null) {
			return verify((FolFormula) d.getFormula().complement());
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
