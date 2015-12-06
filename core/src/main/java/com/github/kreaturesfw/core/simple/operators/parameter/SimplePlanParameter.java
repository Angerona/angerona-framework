package com.github.kreaturesfw.core.simple.operators.parameter;

import javax.management.AttributeNotFoundException;

import com.github.kreaturesfw.core.bdi.components.PlanComponent;
import com.github.kreaturesfw.core.bdi.operators.parameter.PlanParameter;
import com.github.kreaturesfw.core.error.ConversionException;
import com.github.kreaturesfw.core.operators.parameter.GenericOperatorParameter;

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
