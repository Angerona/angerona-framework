package angerona.fw.util;

import angerona.fw.Agent;
import angerona.fw.AngeronaEnvironment;

public interface SimulationListener {
	/** is called when a new simulation starts */
	void simulationStarted(AngeronaEnvironment simulationEnvironment);
	
	/** is called when the given agent is added to the simulation-environment */
	void agentAdded(AngeronaEnvironment simulationEnvironment, Agent added);
	
	/** is called when the given agent is removed from the simulation-environment. */
	void agentRemoved(AngeronaEnvironment simulationEnvironment, Agent removed);
	
	/** is called after a complete cylce of the simulation (an update of ever agent) is done. */
	void tickDone(AngeronaEnvironment simulationEnvironment, boolean finished);
}
