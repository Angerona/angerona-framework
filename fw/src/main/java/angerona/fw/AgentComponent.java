package angerona.fw;

import java.util.Map;

import angerona.fw.internal.Entity;

/**
 * An AgentComponent is an part of an Agent. Every data component of
 * the agent implements the agent component interface. The belief bases are
 * the most important agent components. But the AgentComponent
 * interface is also implemented by the SecrecyKnowledge or the
 * agent's PlanComponent. 
 * 
 * Every agent component supports a copy mechanism which copies all the data of the
 * component but uses the same id like the original object. It also saves it's copy
 * depth
 * 
 * @author Tim Janus
 */
public interface AgentComponent extends Entity{
	
	/**
	 * Makes a copy of the agent component. An components clone duplicates
	 * every data defined in subclasses.
	 * The id and parent id are the same for every clone.  
	 * @return	A deep copy of this object holding the same id like the sourc.
	 */
	public AgentComponent clone();
	
	/**
	 * Defines how often the component was cloned. Is it a copy (1),
	 * a copy of a copy (2), or even the copy of a copy of a copy (3).
	 * @return	an integer representing the copy depth.
	 */
	public int getCopyDepth();
	
	/** 
	 * 	is called after the agent is fully created to initialize the component 
	 * 	@param	additionalData	a map containing the additional data defined in the simulation xml file
	 */
	void init(Map<String, String> additionalData);
	
	/**
	 * @return 	the agent which is the owner of this agent-component or null if
	 * 			the component is not added to an agent yet.
	 */
	Agent getAgent();
	
	/**
	 * Changes the id for the parent.
	 * @param id
	 */
	void setParent(Long id);
}
