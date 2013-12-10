package com.github.angerona.fw.am.secrecy.operators;

import com.github.angerona.fw.Agent;
import com.github.angerona.fw.am.secrecy.operators.parameter.GenerateOptionsParameter;
import com.github.angerona.fw.logic.Desires;
import com.github.angerona.fw.operators.Operator;
import com.github.angerona.fw.util.Pair;


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
