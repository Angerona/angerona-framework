package angerona.fw.operators.parameter;

import java.util.Map;
import java.util.Set;

import net.sf.tweety.Formula;
import angerona.fw.Perception;
import angerona.fw.Skill;
import angerona.fw.logic.base.Beliefs;

/**
 * Class encoding the input parameter for the GenerateOptionsOperator.
 * @author Tim Janus
 */
public class GenerateOptionsParameter {
	/** beliefs of the agent */
	protected Beliefs beliefs;
	
	/** desires of the agent */
	protected Set<Formula> desires;
	
	/** the last received perception */
	protected Perception perception;
	
	protected Map<String, Skill> skills;
	
	/**
	 * Ctor: Generates a GenerateOptionsParameter data-structure with the following parameters:
	 * @param beliefbase	beliefs of the agent
	 * @param desires		desieres of the agent
	 * @param perception	the last received perception
	 */
	public GenerateOptionsParameter(Beliefs beliefbase, Set<Formula> desires, Perception perception, Map<String, Skill> skills) {
		this.beliefs = beliefbase;
		this.desires = desires;
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
