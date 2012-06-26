package angerona.fw;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.beenuts.ap.AgentProcess;

/**
 * Behavior implementing the default Angerona-Environment behavior.
 * runOneTick will wait till isSimulationReady returns true.
 * @author Tim Janus
 */
public class DefaultBehavior implements EnvironmentBehavior  {

	private static Logger LOG = LoggerFactory.getLogger(DefaultBehavior.class);
	
	
	protected boolean angeronaReady = true;
	
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
		env.doingTick = true;
		
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
		
		if(!somethingHappens && env.tick != 0)
			return false;
		
		angeronaReady = false;
		++env.tick;
		for(AgentProcess ap : env.agents) {
			AngeronaAgentProcess aap = (AngeronaAgentProcess)ap;
			aap.execCycle();
			Agent ag = (Agent)aap.getAgentArchitecture();
			if(ag.getLastAction() != null)
				sendAction(env, ag.getLastAction());
		}
		angeronaReady = true;
		
		env.doingTick = false;
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

}
