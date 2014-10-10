package com.github.angerona.fw.motivation.dao.impl;

import net.sf.tweety.logics.fol.syntax.FolFormula;

import com.github.angerona.fw.Desire;
import com.github.angerona.fw.logic.AnswerValue;
import com.github.angerona.fw.logic.Beliefs;
import com.github.angerona.fw.motivation.dao.ActionSequenceDao;
import com.github.angerona.fw.motivation.dao.BeliefState;
import com.github.angerona.fw.motivation.plans.ActionSequence;
import com.github.angerona.fw.motivation.plans.TimeSlots;

/**
 * 
 * @author Manuel Barbi
 *
 */
public class BelieveStateImpl implements BeliefState {

	private Beliefs beliefs;
	private ActionSequenceDao sequences;
	private TimeSlots timeSlots;

	public BelieveStateImpl(Beliefs beliefs, ActionSequenceDao sequences, TimeSlots timeSlots) {
		if (beliefs == null) {
			throw new NullPointerException("beliefs must not be null");
		}

		if (sequences == null) {
			throw new NullPointerException("sequences must not be null");
		}

		if (timeSlots == null) {
			throw new NullPointerException("time-slots must not be null");
		}

		this.beliefs = beliefs;
		this.sequences = sequences;
		this.timeSlots = timeSlots;
	}

	@Override
	public boolean isReliable(Desire d) {
		ActionSequence sequence = sequences.getSequence(d);
		Integer minSlot = timeSlots.getMinSlot();

		// check weather any sequence exists
		if (sequence == null) {
			return false;
		}

		// check weather shortest sequence is smaller than minimal time-slot
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
