package angerona.fw.operators.parameter;

import angerona.fw.Agent;
import angerona.fw.AngeronaAtom;

/**
 * This parameter class is used by the update operator as INPUT type.
 * 
 * @author Tim Janus
 */
public class UpdateBeliefsParameter extends GenericOperatorParameter {
	
	/**	reference to the agent which should perform the update */
	private Agent agent;
	
	/** object representing the perception */
	private AngeronaAtom perception;
	
	/**
	 * Ctor: generating the UpdateParameter data-structure with the following parameters:
	 * @param agent			reference to the agent which should perform the update 
	 * @param perception	object representing the perception which causes the update.
	 */
	public UpdateBeliefsParameter(Agent agent, AngeronaAtom perception) {
		super(agent.getEnvironment());
		this.agent = agent;
		this.perception = perception;
	}
	
	/**	@return reference to the agent which should perform the update */
	public Agent getAgent() {
		return agent;
	}
	
	/** @return object representing the perception */
	public AngeronaAtom getPerception() {
		return perception;
	}
}
