package angerona.fw.operators.parameter;

import java.util.List;

import angerona.fw.Action;
import angerona.fw.MasterPlan;

/**
 * These are the parameters used by a Planer.
 * @author Tim Janus
 */
public class SubgoalGenerationParameter extends GenericOperatorParameter {
	/** a list of possible atomic actions (skills) */
	private List<Action> forbiddenActions;
	
	/** the actual working goals */
	private MasterPlan actualPlan;

	/**
	 * Ctor: Generation the PlanerParameter data-structure with the following parameters:
	 * @param actualPlan	the high level plan of the agent
	 * @param forbidden		list of forbidden actions (forbidden skills)
	 */
	public SubgoalGenerationParameter(MasterPlan actualPlan, List<Action> forbidden) {
		super(actualPlan.getAgent());
		this.forbiddenActions = forbidden;
		this.actualPlan = actualPlan;
	}
	

	/** @return a list of possible actions which were calculated before running the planer */
	public List<Action> getForbiddenActions() {
		return forbiddenActions;
	}
	
	public MasterPlan getActualPlan() {
		return actualPlan;
	}
	
}
