package com.github.angerona.fw.listener;

import com.github.angerona.fw.BaseAgentComponent;
import com.github.angerona.fw.BaseBeliefbase;
import com.github.angerona.fw.Perception;
import com.github.angerona.fw.logic.Beliefs;

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
