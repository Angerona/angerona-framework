package com.github.kreatures.secrecy.operators;

import com.github.kreatures.core.Agent;
import com.github.kreatures.secrecy.operators.parameter.GenerateOptionsParameter;
import com.github.kreatures.core.logic.Desires;
import com.github.kreatures.core.operators.Operator;
import com.github.kreatures.core.util.Pair;


/**
 * Base class for option generation operators, sub-classes of this class
 * are responsible to generate the agents' desires.
 * 
 * Subclasses might depend on the {@link Desires} component of the agent and adapt this
 * component.
 * 
 * It input parameters contain the perception of the current tick, such that
 * the operator can react to the current perceived perceptions.
 * 
 * The operator outputs an integer that represents the count of newly generated
 * desires.
 *
 * @author Tim Janus
 */
public abstract class BaseGenerateOptionsOperator extends 
	Operator<Agent, GenerateOptionsParameter, Integer> {

	public static final String OPERATION_TYPE = "GenerateOptions";
	
	
	@Override
	public Pair<String, Class<?>> getOperationType() {
		return new Pair<String, Class<?>>(OPERATION_TYPE, 
				BaseGenerateOptionsOperator.class);
	}
	
	
	@Override
	protected GenerateOptionsParameter getEmptyParameter() {
		return new GenerateOptionsParameter();
	}


	@Override
	protected Integer defaultReturnValue() {
		return 0;
	}
}
