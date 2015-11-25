package com.github.kreatures.simple.operators;

import com.github.kreatures.secrecy.operators.BaseSubgoalGenerationOperator;
import com.github.kreatures.secrecy.operators.parameter.PlanParameter;
import com.github.kreatures.simple.operators.parameter.SimplePlanParameter;

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
