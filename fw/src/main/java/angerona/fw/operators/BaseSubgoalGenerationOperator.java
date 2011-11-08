package angerona.fw.operators;

import angerona.fw.operators.parameter.SubgoalGenerationParameter;

/**
 * Base class for all subgoal-generation implementations.
 * it returns true if a new subgoal was generated otherwise false.
 * @author Tim Janus
 */
public abstract class BaseSubgoalGenerationOperator 
	extends Operator<SubgoalGenerationParameter, Boolean> {
}
