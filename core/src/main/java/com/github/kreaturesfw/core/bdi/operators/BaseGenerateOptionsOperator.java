package com.github.kreaturesfw.core.bdi.operators;

import com.github.kreaturesfw.core.basic.BaseOperator;
import com.github.kreaturesfw.core.bdi.components.Desires;
import com.github.kreaturesfw.core.bdi.operators.parameter.GenerateOptionsParameter;
import com.github.kreaturesfw.core.legacy.Agent;
import com.github.kreaturesfw.core.util.Pair;


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
	BaseOperator<Agent, GenerateOptionsParameter, Integer> {

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
