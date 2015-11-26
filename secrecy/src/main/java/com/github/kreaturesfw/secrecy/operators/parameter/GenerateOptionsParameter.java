package com.github.kreaturesfw.secrecy.operators.parameter;

import javax.management.AttributeNotFoundException;

import com.github.kreaturesfw.core.error.ConversionException;
import com.github.kreaturesfw.core.legacy.Agent;
import com.github.kreaturesfw.core.legacy.Perception;
import com.github.kreaturesfw.core.operators.parameter.GenericOperatorParameter;
import com.github.kreaturesfw.core.operators.parameter.OperatorPluginParameter;

/**
 * Class encoding the input parameter for the GenerateOptionsOperator.
 * @author Tim Janus
 */
public class GenerateOptionsParameter extends OperatorPluginParameter {

	/** the last received perception */
	protected Perception perception;
	
	public GenerateOptionsParameter() {}
	
	/**
	 * Ctor: Generates a GenerateOptionsParameter data-structure with the following parameters:
	 * @param agent			The agent containing the desires and beliefs.
	 * @param perception	the last received perception
	 */
	public GenerateOptionsParameter(Agent agent, Perception perception) {
		super(agent);
		this.perception = perception;
	}
	
	/** @return the last received perception */
	public Perception getPerception() {
		return perception;
	}
	
	@Override
	public void fromGenericParameter(GenericOperatorParameter gop) 
		throws ConversionException, AttributeNotFoundException {
		super.fromGenericParameter(gop);
		
		Object obj = gop.getParameter("perception");
		if(obj != null) {
			if(! (obj instanceof Perception)) {
				throw conversionException("perception", Perception.class);
			}
			this.perception= (Perception)obj;
		}
	}
}
