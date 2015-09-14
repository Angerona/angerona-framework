package com.github.angerona.fw.simple.behavior;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.angerona.fw.Action;
import com.github.angerona.fw.Agent;
import com.github.angerona.fw.AngeronaEnvironment;
import com.github.angerona.fw.Perception;
import com.github.angerona.fw.def.DefaultBehavior;

/**
 * 
 * @author Manuel Barbi
 *
 */
public abstract class SimpleBehavior<P> extends DefaultBehavior {

	private static final Logger LOG = LoggerFactory.getLogger(SimpleBehavior.class);

	@Override
	public abstract void sendAction(AngeronaEnvironment env, Action act);

	@Override
	public void receivePerception(AngeronaEnvironment env, Perception percept) {}

	@Override
	public boolean runOneTick(AngeronaEnvironment env) {
		P param = runEnvironment();
		return runAgentCycles(env, param);
	}

	protected boolean runAgentCycles(AngeronaEnvironment env, P param) {
		boolean somethingHappend = false;
		Perception perception;

		for (Agent agent : env.getAgents()) {
			if (cycleCondition(env, agent, param)) {
				somethingHappend = true;
				perception = createPerception(env, agent, param);
				LOG.debug("create perception: {}", perception);
				agent.perceive(perception);
				LOG.debug("call agent cycle");
				agent.cycle();
				postCycle(env, agent, param);
			}
		}

		return somethingHappend;
	}

	protected abstract P runEnvironment();

	protected abstract boolean cycleCondition(AngeronaEnvironment env, Agent agent, P param);

	protected abstract Perception createPerception(AngeronaEnvironment env, Agent agent, P param);

	protected abstract void postCycle(AngeronaEnvironment env, Agent agent, P param);

}
