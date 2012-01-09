package angerona.fw.operators.parameter;

import java.util.Map;
import java.util.Set;

import net.sf.tweety.Formula;
import angerona.fw.Agent;
import angerona.fw.Perception;
import angerona.fw.Skill;
import angerona.fw.logic.base.Beliefs;

/**
 * Class encoding the input parameter for the GenerateOptionsOperator.
 * @author Tim Janus
 */
public class GenerateOptionsParameter extends GenericOperatorParameter {
	/** beliefs of the agent */
	protected Beliefs beliefs;
	
	/** desires of the agent */
	protected Set<Formula> desires;
	
	/** the last received perception */
	protected Perception perception;
	
	protected Map<String, Skill> skills;
	
	/**
	 * Ctor: Generates a GenerateOptionsParameter data-structure with the following parameters:
	 * @param agent			The agent containing the desires and beliefs.
	 * @param perception	the last received perception
	 */
	public GenerateOptionsParameter(Agent agent, Perception perception, Map<String, Skill> skills) {
		super(agent.getEnvironment());
		this.beliefs = agent.getBeliefs();
		this.desires = agent.getDesires();
		this.perception = perception;
		this.skills = skills;
	}
	
	/** @return beliefs of the agent */
	public Beliefs getBeliefs() {
		return beliefs;
	}
	
	/** @return desires of the agent */
	public Set<Formula> getDesires() {
		return desires;
	}
	
	/** @return the last received perception */
	public Perception getPerception() {
		return perception;
	}
	
	public Map<String, Skill> getSkills() {
		return skills;
	}
}
