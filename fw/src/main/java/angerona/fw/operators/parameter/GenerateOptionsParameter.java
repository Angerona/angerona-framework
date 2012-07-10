package angerona.fw.operators.parameter;

import java.util.Map;
import java.util.Set;

import angerona.fw.Agent;
import angerona.fw.Desire;
import angerona.fw.Perception;
import angerona.fw.Skill;
import angerona.fw.logic.Beliefs;

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
	
	/** @return beliefs of the agent 
	 * @deprecated*/
	public Beliefs getBeliefs() {
		return agent.getBeliefs();
	}
	
	/** @return desires of the agent 
	 * @deprecated*/
	public Set<Desire> getDesires() {
		return agent.getDesires().getDesires();
	}
	
	/** @return the last received perception */
	public Perception getPerception() {
		return perception;
	}
	
	/**
	 * @return the skills of the agent
	 * @deprecated
	 */
	public Map<String, Skill> getSkills() {
		return agent.getSkills();
	}
	
	public Agent getAgent() {
		return agent;
	}
}
