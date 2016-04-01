package com.github.kreatures.core.listener;

import com.github.kreatures.core.Action;
import com.github.kreatures.core.Agent;
import com.github.kreatures.core.KReaturesEnvironment;

/**
 * Default implementation of the SimulationListener which can act as super
 * class for simulation listeners which does not want to implement all the
 * methods of the listener.
 * 
 * @author Tim Janus
 */
public class SimulationAdapter implements SimulationListener {

	@Override
	public void simulationStarted(KReaturesEnvironment simulationEnvironment) {}

	@Override
	public void simulationDestroyed(KReaturesEnvironment simulationEnvironment) {}

	@Override
	public void agentAdded(KReaturesEnvironment simulationEnvironment,
			Agent added) {}

	@Override
	public void agentRemoved(KReaturesEnvironment simulationEnvironment,
			Agent removed) {}
	
	@Override
	public void tickStarting(KReaturesEnvironment simulationEnvironment) {}
	
	@Override
	public void tickDone(KReaturesEnvironment simulationEnvironment) {}

	@Override
	public void actionPerformed(Agent agent, Action act) {}
}
