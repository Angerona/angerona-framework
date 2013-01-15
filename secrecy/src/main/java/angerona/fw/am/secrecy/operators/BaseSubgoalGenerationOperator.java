package angerona.fw.am.secrecy.operators;

import angerona.fw.Agent;
import angerona.fw.am.secrecy.operators.parameter.PlanParameter;
import angerona.fw.operators.Operator;
import angerona.fw.util.Pair;


/**
 * Base class for all subgoal-generation implementations.
 * it returns true if a new subgoal was generated otherwise false.
 * @author Tim Janus
 */
public abstract class BaseSubgoalGenerationOperator 
	extends Operator<Agent, PlanParameter, Boolean> {

	public static final String OPERATION_NAME = "SubgoalGeneration";
	
	@Override
	public Pair<String, Class<?>> getOperationType() {
		return new Pair<String, Class<?>>(OPERATION_NAME,
				BaseSubgoalGenerationOperator.class);
	}
	
	@Override
	protected PlanParameter getEmptyParameter() {
		return new PlanParameter();
	}

	@Override
	protected Boolean defaultReturnValue() {
		return false;
	}
	
}
