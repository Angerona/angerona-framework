package angerona.fw.listener;

import angerona.fw.Action;
import angerona.fw.Agent;
import angerona.fw.AngeronaEnvironment;

/**
 * Listener interface which gets informed about changes of the simulation. For example if a new agent
 * is added to simulation. Or an agent is removed, when the simulation starts or a tick is done.
 * 
 * @author Tim Janus
 */
public interface SimulationListener {	
	/** is called when a new simulation starts */
	void simulationStarted(AngeronaEnvironment simulationEnvironment);
	
	/** is called when a simulations cleanup method is called */
	void simulationDestroyed(AngeronaEnvironment simulationEnvironment);
	
	/** is called when the given agent is added to the simulation-environment */
	void agentAdded(AngeronaEnvironment simulationEnvironment, Agent added);
	
	/** is called when the given agent is removed from the simulation-environment. */
	void agentRemoved(AngeronaEnvironment simulationEnvironment, Agent removed);
	
	/** is called after a complete cylce of the simulation (an update of ever agent) is done. */
	void tickDone(AngeronaEnvironment simulationEnvironment, boolean finished);

	/** is called after an agent has performed an action. */
	void actionPerformed(Agent agent, Action act);
}
