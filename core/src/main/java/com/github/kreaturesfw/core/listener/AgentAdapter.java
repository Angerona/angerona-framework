package com.github.kreaturesfw.core.listener;

import com.github.kreaturesfw.core.AgentComponent;
import com.github.kreaturesfw.core.BaseBeliefbase;
import com.github.kreaturesfw.core.Perception;
import com.github.kreaturesfw.core.logic.Beliefs;

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
	public void componentAdded(AgentComponent comp) {}

	@Override
	public void componentRemoved(AgentComponent comp) {}
	
	@Override
	public void componentInitialized(AgentComponent comp) {}
}
