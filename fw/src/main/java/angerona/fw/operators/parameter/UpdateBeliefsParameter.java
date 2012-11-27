package angerona.fw.operators.parameter;

import angerona.fw.Agent;
import angerona.fw.AngeronaAtom;
import angerona.fw.logic.Beliefs;

/**
 * This parameter class is used by the update operator as INPUT type.
 * 
 * @author Tim Janus
 */
public class UpdateBeliefsParameter extends GenericOperatorParameter {
	
	private Beliefs beliefs;
	
	/** object representing the perception */
	private AngeronaAtom perception;
	
	/**
	 * Ctor: generating the UpdateParameter data-structure with the following parameters:
	 * @param agent			reference to the agent which should perform the update 
	 * @param perception	object representing the perception which causes the update.
	 */
	public UpdateBeliefsParameter(Agent agent, AngeronaAtom perception) {
		this(agent, agent.getBeliefs(), perception);
	}
	
	public UpdateBeliefsParameter(Agent agent, Beliefs beliefs, AngeronaAtom perception) {
		super(agent);
		this.beliefs = beliefs;
		this.perception = perception;
	}
	
	public Beliefs getBeliefs() {
		return beliefs;
	}
	
	/** @return object representing the perception */
	public AngeronaAtom getPerception() {
		return perception;
	}
}
