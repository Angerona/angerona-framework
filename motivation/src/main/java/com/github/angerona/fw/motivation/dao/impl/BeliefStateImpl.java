package com.github.angerona.fw.motivation.dao.impl;

import net.sf.tweety.logics.fol.syntax.FolFormula;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.angerona.fw.Desire;
import com.github.angerona.fw.logic.AnswerValue;
import com.github.angerona.fw.logic.Beliefs;
import com.github.angerona.fw.motivation.dao.BeliefState;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class BeliefStateImpl implements BeliefState<FolFormula> {

	private static final Logger LOG = LoggerFactory.getLogger(BeliefStateImpl.class);

	protected Beliefs beliefs;

	public BeliefStateImpl(Beliefs beliefs) {
		if (beliefs == null) {
			throw new NullPointerException("beliefs must not be null");
		}

		this.beliefs = beliefs;

		LOG.debug("created {}", this.getClass().getSimpleName());
	}

	@Override
	public boolean isReliable(Desire d) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isSatisfied(Desire d) {
		return verify((FolFormula) d.getFormula().complement());
	}

	@Override
	public boolean verify(FolFormula statement) {
		return (beliefs.getWorldKnowledge().reason(statement).getAnswerValue() == AnswerValue.AV_TRUE);
	}

}
