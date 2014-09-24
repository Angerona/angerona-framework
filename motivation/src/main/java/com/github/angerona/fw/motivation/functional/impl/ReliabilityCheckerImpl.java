package com.github.angerona.fw.motivation.functional.impl;

import static com.github.angerona.fw.motivation.utils.FormulaUtils.desireToString;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.angerona.fw.Agent;
import com.github.angerona.fw.Desire;
import com.github.angerona.fw.motivation.dao.ActionComponentDao;
import com.github.angerona.fw.motivation.functional.ReliabilityChecker;
import com.github.angerona.fw.motivation.island.comp.Battery;
import com.github.angerona.fw.motivation.island.comp.IslandActions;
import com.github.angerona.fw.motivation.island.enums.ActionId;
import com.github.angerona.fw.motivation.plan.ActionSequence;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class ReliabilityCheckerImpl implements ReliabilityChecker {

	private static final Logger LOG = LoggerFactory.getLogger(ReliabilityCheckerImpl.class);

	protected Agent agent;

	public ReliabilityCheckerImpl(Agent agent) {
		if (agent == null) {
			throw new NullPointerException("agent must not be null");
		}

		this.agent = agent;
	}

	@Override
	public boolean reliable(Desire d) {
		boolean reliable = false;

		ActionComponentDao<ActionId> actions = agent.getComponent(IslandActions.class);
		ActionSequence<ActionId> seq = actions.get(d);
		Battery battery = agent.getComponent(Battery.class);

		boolean safe = false;
		boolean charge = false;

		// check whether there is a possible action sequence at all
		// check if sequence can be performed, before thunderstorm or unknown weather
		// check if sequence can be performed before battery charge is empty

		if (seq != null) {
			safe = !(seq.getDuration() > seq.getSafeWindow());
			charge = (seq.getDuration() + 1 < battery.getCharge());
		}

		reliable = safe && charge;
		agent.report("reliable(" + desireToString(d) + "): " + reliable);

		return reliable;
	}

}
