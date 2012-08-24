package angerona.fw.listener;

import angerona.fw.BaseAgentComponent;
import angerona.fw.BaseBeliefbase;
import angerona.fw.Perception;

/**
 * 
 * @author Tim Janus
 */
public interface AgentListener {
	public static String WORLD = "WORLD";
	
	/**
	 * informs the listener of the agent if a belief base was changed.
	 * @param bb 		reference to the changed belief base.
	 * @param percept	The perception causing the change of the belief-base.
	 * @param space 	a string explaining the space of the beliefbase
	 * 					this might be "WORLD" for the beliefs of the 
	 * 					agent himself or "alice" indicating this as an 
	 * 					view on alices beliefs.
	 */
	void beliefbaseChanged(BaseBeliefbase bb, Perception percept, String space);
	
	void componentAdded(BaseAgentComponent comp);
	
	void componentRemoved(BaseAgentComponent comp);
}
