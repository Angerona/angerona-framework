package com.github.kreaturesfw.simple.operators;

import com.github.kreaturesfw.secrecy.operators.BaseSubgoalGenerationOperator;
import com.github.kreaturesfw.secrecy.operators.parameter.PlanParameter;
import com.github.kreaturesfw.simple.operators.parameter.SimplePlanParameter;

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
