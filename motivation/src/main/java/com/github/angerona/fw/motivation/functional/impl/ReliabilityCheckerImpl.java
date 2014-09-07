package com.github.angerona.fw.motivation.functional.impl;

import static com.github.angerona.fw.motivation.utils.FolReasonUtils.reason;
import net.sf.tweety.logics.fol.syntax.FolFormula;

import com.github.angerona.fw.Agent;
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

	protected Agent agent;

	public ReliabilityCheckerImpl(Agent agent) {
		if (agent == null) {
			throw new NullPointerException("agent must not be null");
		}
	}

	@Override
	public boolean reliable(Desire d) {
		// TODO: check sicheres zeitfenster und batterystand
		return true;
	}

}
