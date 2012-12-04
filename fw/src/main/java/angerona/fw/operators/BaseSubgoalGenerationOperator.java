package angerona.fw.operators;

import angerona.fw.operators.parameter.PlanParameter;


/**
 * Base class for all subgoal-generation implementations.
 * it returns true if a new subgoal was generated otherwise false.
 * @author Tim Janus
 */
public abstract class BaseSubgoalGenerationOperator 
	extends Operator<PlanParameter, Boolean> {

	@Override
	protected PlanParameter getEmptyParameter() {
		return new PlanParameter();
	}

	@Override
	protected Boolean defaultReturnValue() {
		return false;
	}
	
}
