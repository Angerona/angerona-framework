package angerona.fw.operators.parameter;

import angerona.fw.Agent;
import angerona.fw.Perception;

/**
 * Class encoding the input parameter for the GenerateOptionsOperator.
 * @author Tim Janus
 */
public class GenerateOptionsParameter extends GenericOperatorParameter {
	protected Agent agent;
	
	/** the last received perception */
	protected Perception perception;
	
	/**
	 * Ctor: Generates a GenerateOptionsParameter data-structure with the following parameters:
	 * @param agent			The agent containing the desires and beliefs.
	 * @param perception	the last received perception
	 */
	public GenerateOptionsParameter(Agent agent, Perception perception) {
		super(agent);
		this.agent = agent;
		this.perception = perception;
	}
	
	/** @return the last received perception */
	public Perception getPerception() {
		return perception;
	}
}
