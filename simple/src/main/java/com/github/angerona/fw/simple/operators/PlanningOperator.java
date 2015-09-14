package com.github.angerona.fw.simple.operators;

import com.github.angerona.fw.am.secrecy.operators.BaseSubgoalGenerationOperator;
import com.github.angerona.fw.am.secrecy.operators.parameter.PlanParameter;
import com.github.angerona.fw.simple.operators.parameter.SimplePlanParameter;

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
