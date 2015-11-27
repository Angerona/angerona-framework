package com.github.kreaturesfw.core.simple.behavior;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.kreaturesfw.core.KReatures;
import com.github.kreaturesfw.core.KReaturesEnvironment;
import com.github.kreaturesfw.core.basic.Action;
import com.github.kreaturesfw.core.basic.Agent;
import com.github.kreaturesfw.core.basic.Perception;
import com.github.kreaturesfw.core.def.DefaultBehavior;

/**
 * 
 * @author Manuel Barbi
 *
 */
public abstract class SimpleBehavior extends DefaultBehavior {

	private static final Logger LOG = LoggerFactory.getLogger(SimpleBehavior.class);

	@Override
	public abstract void sendAction(KReaturesEnvironment env, Action act);

	@Override
	public void receivePerception(KReaturesEnvironment env, Perception percept) {
	}

	@Override
	public boolean runOneTick(KReaturesEnvironment env) {
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
		KReatures.getInstance().onTickStarting(env);

		// actual execution

		runEnvironment();
		somethingHappens = runAgentCycles(env);

		// synchronization stuff

		angeronaReady = true;

		doingTick = false;
		KReatures.getInstance().onTickDone(env);

		return somethingHappens;
	}

	protected boolean runAgentCycles(KReaturesEnvironment env) {
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
					KReatures.getInstance().onError(e.getClass().getSimpleName(), e.getMessage() != null ? e.getMessage() : "<no message>");
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
	protected abstract boolean cycleCondition(KReaturesEnvironment env, Agent agent);

	protected abstract Perception createPerception(KReaturesEnvironment env, Agent agent);

	protected abstract void postCycle(KReaturesEnvironment env, Agent agent);

	/**
	 * 
	 * @param env
	 * @param agent
	 * @return weather the agent's cycle should be called again
	 */
	protected abstract boolean terminationCriterion(KReaturesEnvironment env, Agent agent);

}
