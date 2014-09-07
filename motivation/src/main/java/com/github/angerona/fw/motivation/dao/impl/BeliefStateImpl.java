package com.github.angerona.fw.motivation.dao.impl;

import static com.github.angerona.fw.motivation.utils.FolReasonUtils.reason;
import net.sf.tweety.logics.fol.syntax.FolFormula;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.angerona.fw.Desire;
import com.github.angerona.fw.logic.Beliefs;
import com.github.angerona.fw.motivation.dao.BeliefState;
import com.github.angerona.fw.motivation.functional.ReliabilityChecker;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class BeliefStateImpl implements BeliefState<FolFormula> {

	private static final Logger LOG = LoggerFactory.getLogger(BeliefStateImpl.class);

	protected Beliefs beliefs;
	protected ReliabilityChecker checker;

	public BeliefStateImpl(Beliefs beliefs, ReliabilityChecker checker) {
		if (beliefs == null) {
			throw new NullPointerException("beliefs must not be null");
		}

		if (checker == null) {
			throw new NullPointerException("reliability-checker must not be null");
		}

		this.beliefs = beliefs;
		this.checker = checker;

		LOG.debug("created {}", this.getClass().getSimpleName());
	}

	@Override
	public boolean isReliable(Desire d) {
		return checker.reliable(d);
	}

	@Override
	public boolean isSatisfied(Desire d) {
		return verify((FolFormula) d.getFormula().complement());
	}

	@Override
	public boolean verify(FolFormula statement) {
		return reason(beliefs, statement);
	}

}
