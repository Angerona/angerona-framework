package com.github.kreaturesfw.core.simple.operators;

import com.github.kreaturesfw.core.bdi.operators.BaseSubgoalGenerationOperator;
import com.github.kreaturesfw.core.bdi.operators.parameter.PlanParameter;
import com.github.kreaturesfw.core.simple.operators.parameter.SimplePlanParameter;

/**
 * 
 * @author Manuel Barbi
 *
 */
public abstract class PlanningOperator extends BaseSubgoalGenerationOperator {

	@Override
	protected PlanParameter getEmptyParameter() {
		return new SimplePlanParameter();
	}
	
}
