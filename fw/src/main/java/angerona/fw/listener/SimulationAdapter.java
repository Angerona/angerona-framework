package angerona.fw.listener;

import angerona.fw.Action;
import angerona.fw.Agent;
import angerona.fw.AngeronaEnvironment;

/**
 * Default implementation of the SimulationListener which can act as super
 * class for simulation listeners which does not want to implement all the
 * methods of the listener.
 * 
 * @author Tim Janus
 */
public class SimulationAdapter implements SimulationListener {

	@Override
	public void simulationStarted(AngeronaEnvironment simulationEnvironment) {}

	@Override
	public void simulationDestroyed(AngeronaEnvironment simulationEnvironment) {}

	@Override
	public void agentAdded(AngeronaEnvironment simulationEnvironment,
			Agent added) {}

	@Override
	public void agentRemoved(AngeronaEnvironment simulationEnvironment,
			Agent removed) {}

	@Override
	public void tickDone(AngeronaEnvironment simulationEnvironment,
			boolean finished) {}

	@Override
	public void actionPerformed(Agent agent, Action act) {}

}
