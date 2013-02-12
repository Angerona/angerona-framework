package angerona.fw.def;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.Action;
import angerona.fw.Agent;
import angerona.fw.AngeronaAgentProcess;
import angerona.fw.AngeronaEnvironment;
import angerona.fw.EnvironmentBehavior;
import angerona.fw.Perception;

import net.sf.beenuts.ap.AgentProcess;

/**
 * Behavior implementing the default Angerona environment behavior.
 * runOneTick will wait till isSimulationReady returns true.
 * @author Tim Janus
 */
public class DefaultBehavior implements EnvironmentBehavior  {

	private static Logger LOG = LoggerFactory.getLogger(DefaultBehavior.class);
	
	protected boolean doingTick = false;
	
	protected boolean angeronaReady = true;
	
	/** the actual simulation tick */
	protected int tick = 0;
	
	
	@Override
	public void sendAction(AngeronaEnvironment env, Action act) {
		// The action send by one agent is the perception of the other one.
		String agentName = act.getReceiverId();
		localDelegate(env, act, agentName);
	}

	@Override
	public void receivePerception(AngeronaEnvironment env, Perception percept) {
		String agentName = percept.getReceiverId();
		localDelegate(env, percept, agentName);
	}
	
	/**
	 * Helper method: delegates the percetion/action to the local agents.
	 * @param env
	 * @param percept
	 * @param agentName
	 */
	protected void localDelegate(AngeronaEnvironment env, Perception percept, String agentName) {
		if(Action.ALL.equals(agentName)) {
			for(String name : env.agentMap.keySet()) {
				env.agentMap.get(name).perceive(percept);
			}
		} else {
			if(!env.agentMap.containsKey(agentName))
				LOG.warn("Action/Perception was not send, agent '{}' was not found in environment.", agentName);
			else
				env.agentMap.get(agentName).perceive(percept);
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
		
		boolean somethingHappens = false;
		for(AgentProcess ap : env.agents) {
			AngeronaAgentProcess aap = (AngeronaAgentProcess)ap;
			if(aap.hasPerceptions()) {
				somethingHappens = true;
			}
		}
		
		if(!somethingHappens && tick != 0)
			return false;
		
		angeronaReady = false;
		++tick;
		for(AgentProcess ap : env.agents) {
			AngeronaAgentProcess aap = (AngeronaAgentProcess)ap;
			aap.execCycle();
			Agent ag = (Agent)aap.getAgentArchitecture();
			if(ag.getLastAction() != null)
				sendAction(env, ag.getLastAction());
		}
		angeronaReady = true;
		
		doingTick = false;
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
