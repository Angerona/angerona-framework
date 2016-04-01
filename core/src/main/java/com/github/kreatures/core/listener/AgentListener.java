package com.github.kreatures.core.listener;

import com.github.kreatures.core.AgentComponent;
import com.github.kreatures.core.BaseBeliefbase;
import com.github.kreatures.core.Perception;
import com.github.kreatures.core.logic.Beliefs;

/**
 * The agent listener gets informed if something of an agent changes like the
 * update of its beliefs or of a specific belief base or even if the content of
 * an agent's component changes.
 * 
 * @author Tim Janus
 */
public interface AgentListener {
	
	/** the identifier used to name a world belief base */
	public static final String WORLD = "_WORLD_";
	
	/** 
	 * is called when the agent updates it's beliefs with the given perception.
	 * @param percept		Reference to the perception used for the belief update.
	 * @param oldBeliefs	The beliefs before the update beliefs process is finished
	 * @param newBeliefs	The beliefs of the agent after he update belief process is finished.
	 */
	void updateBeliefs(Perception percept, Beliefs oldBeliefs, Beliefs newBeliefs);
	
	/**
	 * is called when a belief base of an agent changes.
	 * @param bb 		reference to the changed belief base.
	 * @param percept	The perception causing the change of the belief-base.
	 * @param space 	a string explaining the space of the beliefbase
	 * 					this might be "WORLD" for the beliefs of the 
	 * 					agent himself or "alice" indicating this as an 
	 * 					view on alices beliefs.
	 */
	void beliefbaseChanged(BaseBeliefbase bb, Perception percept, String space);
	
	/**
	 * is called when the given component is added to list of components of the agent
	 * @param comp	the added component.
	 */
	void componentAdded(AgentComponent comp);
	
	/**
	 * is called when the given component is removed from the the agent
	 * @param comp	the removed component.
	 */
	void componentRemoved(AgentComponent comp);
	
	void componentInitialized(AgentComponent comp);
}
