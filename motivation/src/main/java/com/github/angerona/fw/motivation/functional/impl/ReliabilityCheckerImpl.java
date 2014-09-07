package com.github.angerona.fw.motivation.functional.impl;

import static com.github.angerona.fw.motivation.utils.FolReasonUtils.reason;
import net.sf.tweety.logics.fol.syntax.FolFormula;

import com.github.angerona.fw.Desire;
import com.github.angerona.fw.logic.Beliefs;
import com.github.angerona.fw.motivation.dao.ActionComponentDao;
import com.github.angerona.fw.motivation.dao.PlanComponentDao;
import com.github.angerona.fw.motivation.functional.ReliabilityChecker;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class ReliabilityCheckerImpl implements ReliabilityChecker {

	protected Beliefs beliefs;
	protected PlanComponentDao<?, FolFormula> plans;
	protected ActionComponentDao<?> actions;

	public ReliabilityCheckerImpl(Beliefs beliefs, PlanComponentDao<?, FolFormula> plans, ActionComponentDao<?> actions) {
		if (beliefs == null) {
			throw new NullPointerException("beliefs must not be null");
		}

		if (plans == null) {
			throw new NullPointerException("plans must not be null");
		}

		if (actions == null) {
			throw new NullPointerException("actions must not be null");
		}

		this.beliefs = beliefs;
		this.plans = plans;
		this.actions = actions;
	}

	@Override
	public boolean reliable(Desire d) {
		FolFormula alt = plans.getRelAlternatives(d);
		FolFormula cond = plans.getRelCondition(d);

		return (alt != null ? reason(beliefs, alt) : true) || (cond != null ? reason(beliefs, cond) : true) && actions.exists(d);
	}

}
