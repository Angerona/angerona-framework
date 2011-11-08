package angerona.fw.operators.parameter;

import angerona.fw.Agent;

/**
 * This parameter class is used by the update operator as INPUT type.
 * 
 * @author Tim Janus
 */
public class UpdateParameter {
	
	/**	reference to the agent which should perform the update */
	private Agent agent;
	
	/** object representing the perception */
	private Object perception;
	
	/**
	 * Ctor: generating the UpdateParameter data-structure with the following parameters:
	 * @param agent			reference to the agent which should perform the update 
	 * @param perception	object representing the perception which causes the update.
	 */
	public UpdateParameter(Agent agent, Object perception) {
		this.agent = agent;
		this.perception = perception;
	}
	
	/**	@return reference to the agent which should perform the update */
	public Agent getAgent() {
		return agent;
	}
	
	/** @return object representing the perception */
	public Object getPerception() {
		return perception;
	}
}
