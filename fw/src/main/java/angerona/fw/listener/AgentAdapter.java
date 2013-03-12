package angerona.fw.listener;

import angerona.fw.BaseAgentComponent;
import angerona.fw.BaseBeliefbase;
import angerona.fw.Perception;
import angerona.fw.logic.Beliefs;

/**
 * The AgentAdapter is an abstract class implementing the AgentListener 
 * interface. It provides empty methods. This class exists as convenience 
 * for creating listener objects.
 * 
 * @author Tim Janus
 */
public abstract class AgentAdapter implements AgentListener {

	@Override
	public void updateBeliefs(Perception percept, Beliefs oldBeliefs, Beliefs newBeliefs) {}

	@Override
	public void beliefbaseChanged(BaseBeliefbase bb, Perception percept,
			String space) {}

	@Override
	public void componentAdded(BaseAgentComponent comp) {}

	@Override
	public void componentRemoved(BaseAgentComponent comp) {}
}
