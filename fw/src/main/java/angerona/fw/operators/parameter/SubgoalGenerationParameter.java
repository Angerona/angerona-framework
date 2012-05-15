package angerona.fw.operators.parameter;

import java.util.List;

import angerona.fw.MasterPlan;
import angerona.fw.Skill;

/**
 * These are the parameters used by a Planer.
 * @author Tim Janus
 */
public class SubgoalGenerationParameter extends GenericOperatorParameter {
	/** a list of possible atomic actions (skills) */
	private List<Skill> skills;
	
	/** the actual working goals */
	private MasterPlan actualPlan;

	/**
	 * Ctor: Generation the PlanerParameter data-structure with the following parameters:
	 * @param actualPlan	the high level plan of the agent
	 * @param options		list of possible actions (applicable skills)
	 */
	public SubgoalGenerationParameter(MasterPlan actualPlan, List<Skill> skills) {
		super(actualPlan.getAgent().getEnvironment());
		this.skills = skills;
		this.actualPlan = actualPlan;
	}
	

	/** @return a list of possible actions which were calculated before running the planer */
	public List<Skill> getOptions() {
		return skills;
	}
	
	public MasterPlan getActualPlan() {
		return actualPlan;
	}
	
}
