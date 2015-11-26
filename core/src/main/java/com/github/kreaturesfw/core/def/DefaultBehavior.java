package com.github.kreaturesfw.core.def;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.kreaturesfw.core.Angerona;
import com.github.kreaturesfw.core.comm.SpeechAct;
import com.github.kreaturesfw.core.legacy.Action;
import com.github.kreaturesfw.core.legacy.Agent;
import com.github.kreaturesfw.core.legacy.AngeronaEnvironment;
import com.github.kreaturesfw.core.legacy.EnvironmentBehavior;
import com.github.kreaturesfw.core.legacy.Perception;

/**
 * Behavior implementing the default Angerona environment behavior.
 * runOneTick will wait till isSimulationReady returns true.
 * @author Tim Janus
 */
public class DefaultBehavior implements EnvironmentBehavior  {

	private static Logger LOG = LoggerFactory.getLogger(DefaultBehavior.class);
	
	protected boolean doingTick = false;
	
	protected boolean angeronaReady = true;
	
	protected boolean somethingHappens = false;
	
	/** the actual simulation tick */
	protected int tick = 0;
	
	
	@Override
	public void sendAction(AngeronaEnvironment env, Action act) {
		// The action send by one agent is the perception of the other one.
		somethingHappens = true;
		
		// forward the action if it can be perceived by other agents
		if(act instanceof Perception) {
			Perception per = (Perception) act;
			String agentName = per.getReceiverId();
			localDelegate(env, per, agentName);
		}
	}

	@Override
	public void receivePerception(AngeronaEnvironment env, Perception percept) {
		String agentName = percept.getReceiverId();
		localDelegate(env, percept, agentName);
	}
	
	/**
	 * Helper method: delegates the perception/action to the local agents.
	 * @param env
	 * @param percept
	 * @param agentName
	 */
	protected void localDelegate(AngeronaEnvironment env, Perception percept, String agentName) {
		if(SpeechAct.ALL.equals(agentName)) {
			for(Agent agent : env.getAgents()) {
				agent.perceive(percept);
			}
		} else {
			Agent ag = env.getAgentByName(agentName);
			if(ag != null) {
				ag.perceive(percept);
			} else {
				LOG.warn("Action/Perception was not send, agent '{}' was not found in environment.", agentName);
			}
		}
	}

	@Override
	public boolean runOneTick(AngeronaEnvironment env) {
		doingTick = true;
		
		while(!isSimulationReady()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		if(!somethingHappens && tick != 0)
			return false;
		
		somethingHappens = false;
		angeronaReady = false;
		++tick;
		Angerona.getInstance().onTickStarting(env);
		
		List<Agent> orderedAlphabetically = new ArrayList<>(env.getAgents());
		Collections.sort(orderedAlphabetically, new Comparator<Agent>() {
			@Override
			public int compare(Agent o1, Agent o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		
		for(Agent agent : orderedAlphabetically) {
			// cycle internally sends the selected action
			// to the environment using sendAction() method.
			agent.cycle();
		}
		angeronaReady = true;
		
		doingTick = false;
		Angerona.getInstance().onTickDone(env);
		return true;
	}

	@Override
	public boolean run(AngeronaEnvironment env) {
		boolean reval = false;
		while(reval = runOneTick(env));
		return reval;
	}

	@Override
	public boolean isAngeronaReady() {
		return angeronaReady;
	}

	@Override
	public boolean isSimulationReady() {
		return true;
	}

	@Override
	public boolean isDoingTick() {
		return doingTick;
	}

	@Override
	public int getTick() {
		return tick;
	}

}
