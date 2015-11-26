package com.github.kreaturesfw.core.simple.behavior;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.kreaturesfw.core.Angerona;
import com.github.kreaturesfw.core.def.DefaultBehavior;
import com.github.kreaturesfw.core.legacy.Action;
import com.github.kreaturesfw.core.legacy.Agent;
import com.github.kreaturesfw.core.legacy.AngeronaEnvironment;
import com.github.kreaturesfw.core.legacy.Perception;

/**
 * 
 * @author Manuel Barbi
 *
 */
public abstract class SimpleBehavior extends DefaultBehavior {

	private static final Logger LOG = LoggerFactory.getLogger(SimpleBehavior.class);

	@Override
	public abstract void sendAction(AngeronaEnvironment env, Action act);

	@Override
	public void receivePerception(AngeronaEnvironment env, Perception percept) {
	}

	@Override
	public boolean runOneTick(AngeronaEnvironment env) {
		doingTick = true;

		// simulation stuff

		while (!isSimulationReady()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		angeronaReady = false;
		++tick;
		Angerona.getInstance().onTickStarting(env);

		// actual execution

		runEnvironment();
		somethingHappens = runAgentCycles(env);

		// synchronization stuff

		angeronaReady = true;

		doingTick = false;
		Angerona.getInstance().onTickDone(env);

		return somethingHappens;
	}

	protected boolean runAgentCycles(AngeronaEnvironment env) {
		boolean doContinue = false;
		Perception perception;

		for (Agent agent : env.getAgents()) {
			if (cycleCondition(env, agent)) {
				try {
					perception = createPerception(env, agent);
					LOG.debug("create perception: {}", perception);
					agent.perceive(perception);
					LOG.debug("call agent cycle");
					agent.cycle();
					postCycle(env, agent);
					doContinue = doContinue || !terminationCriterion(env, agent);
				} catch (Exception e) {
					e.printStackTrace();
					Angerona.getInstance().onError(e.getClass().getSimpleName(), e.getMessage() != null ? e.getMessage() : "<no message>");
				}
			}
		}

		return doContinue;
	}

	/**
	 * 
	 * global execution previous to the agent's cycle
	 */
	protected abstract void runEnvironment();

	/**
	 * 
	 * @param env
	 * @param agent
	 * @return weather the agent's cycle is called
	 */
	protected abstract boolean cycleCondition(AngeronaEnvironment env, Agent agent);

	protected abstract Perception createPerception(AngeronaEnvironment env, Agent agent);

	protected abstract void postCycle(AngeronaEnvironment env, Agent agent);

	/**
	 * 
	 * @param env
	 * @param agent
	 * @return weather the agent's cycle should be called again
	 */
	protected abstract boolean terminationCriterion(AngeronaEnvironment env, Agent agent);

}
