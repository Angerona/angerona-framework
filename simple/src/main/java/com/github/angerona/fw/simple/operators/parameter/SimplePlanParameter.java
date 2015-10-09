package com.github.angerona.fw.simple.operators.parameter;

import javax.management.AttributeNotFoundException;

import com.github.angerona.fw.PlanComponent;
import com.github.angerona.fw.am.secrecy.operators.parameter.PlanParameter;
import com.github.angerona.fw.error.ConversionException;
import com.github.angerona.fw.operators.parameter.GenericOperatorParameter;

/**
 * 
 * @author Manuel Barbi
 *
 */
public class SimplePlanParameter extends PlanParameter {

	@Override
	public PlanComponent getActualPlan() {
		return getAgent().getComponent(PlanComponent.class);
	}

	@Override
	public void fromGenericParameter(GenericOperatorParameter gop) throws ConversionException, AttributeNotFoundException {
		try {
			super.fromGenericParameter(gop);
		} catch (ConversionException e) {
			// this is an unpleasant workaround
		}
	}

}
