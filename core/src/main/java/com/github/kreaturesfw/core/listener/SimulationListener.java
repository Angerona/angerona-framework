package com.github.kreaturesfw.core.listener;

import com.github.kreaturesfw.core.basic.Action;
import com.github.kreaturesfw.core.basic.Agent;
import com.github.kreaturesfw.core.legacy.AngeronaEnvironment;

/**
 * Listener interface which gets informed about changes of the simulation. For example if a new agent
 * is added to simulation. Or an agent is removed, when the simulation starts or a tick is done.
 * 
 * @author Tim Janus
 */
public interface SimulationListener {	
	/** is called when a new simulation starts, before any initialization */
	void simulationStarted(AngeronaEnvironment simulationEnvironment);
	
	/** is called when a simulations cleanup method is called */
	void simulationDestroyed(AngeronaEnvironment simulationEnvironment);
	
	/** is called when the given agent is added to the simulation-environment */
	void agentAdded(AngeronaEnvironment simulationEnvironment, Agent added);
	
	/** is called when the given agent is removed from the simulation-environment. */
	void agentRemoved(AngeronaEnvironment simulationEnvironment, Agent removed);
	
	/** is called when a new tick is starting, before the first agent does anything */
	void tickStarting(AngeronaEnvironment simulationEnvironment);
	
	/** is called after a complete cycle of the simulation (an update of ever agent) is done. */
	void tickDone(AngeronaEnvironment simulationEnvironment);

	/** is called after an agent has performed an action. */
	void actionPerformed(Agent agent, Action act);
	
}
