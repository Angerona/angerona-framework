package angerona.fw.operators.parameter;

import java.util.List;

import angerona.fw.Intention;
import angerona.fw.Plan;

/**
 * These are the parameters used by a Planer.
 * @author Tim Janus
 */
public class SubgoalGenerationParameter {
	/** a list of possible actions which were calculated before running the planer */
	private List<Intention> options;
	
	/** the actual working goals */
	private Plan actualPlan;

	/**
	 * Ctor: Generation the PlanerParameter data-structure with the following parameters:
	 * @param actualPlan	the high level plan of the agent
	 * @param options		list of possible actions (applicable skills)
	 */
	public SubgoalGenerationParameter(Plan actualPlan, List<Intention> options) {
		this.options = options;
		this.actualPlan = actualPlan;
	}
	

	/** @return a list of possible actions which were calculated before running the planer */
	public List<Intention> getOptions() {
		return options;
	}
	
	public Plan getActualPlan() {
		return actualPlan;
	}
	
}
