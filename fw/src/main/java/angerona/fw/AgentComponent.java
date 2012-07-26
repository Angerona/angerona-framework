package angerona.fw;

import java.util.Map;

import angerona.fw.internal.EntityAtomic;

/**
 * An AgentComponent is an part of an Agent. Like its Plan or its Confidential-Knowledge
 * it should be used as base interface for specializations of the agent. 
 * 
 * Other interesting specializations are: Motivations, ect.
 * 
 * @author Tim Janus
 */
public interface AgentComponent extends EntityAtomic{
	
	/** 
	 * 	is called after the agent is fully created to initialize the component 
	 * 	@param	additionalData	a map containing the addiotinal data defined in the simulation xml file
	 */
	void init(Map<String, String> additionalData);
	
	/**
	 * @return 	the agent which is the owner of this agent-component or null if
	 * 			the component is not added to an agent yet.
	 */
	Agent getAgent();
	
	void setParent(Long id);
}
