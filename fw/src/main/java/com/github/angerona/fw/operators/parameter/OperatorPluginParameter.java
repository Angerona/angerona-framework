package com.github.angerona.fw.operators.parameter;

import javax.management.AttributeNotFoundException;

import com.github.angerona.fw.Agent;
import com.github.angerona.fw.error.ConversionException;
import com.github.angerona.fw.operators.OperatorStack;
import com.github.angerona.fw.report.Reporter;

/**
 * Base class for all input parameters for operators defined in the
 * operator plugin.
 * 
 * @author Tim Janus
 */
public class OperatorPluginParameter extends OperatorParameterAdapter {

	/** the agent who calls the operator */
	private Agent caller;
	
	/** Default Ctor: Used for dynamic instantiation */
	public OperatorPluginParameter() {}
	
	public OperatorPluginParameter(Agent caller) {
		if(caller == null) {
			throw new IllegalArgumentException("Caller most not be null.");
		}
		this.caller = caller;
	}
	
	@Override
	public Reporter getReporter() {
		return caller;
	}	
	
	/** @return the agent who class the operator */
	public Agent getAgent() {
		return caller;
	}

	@Override
	public void fromGenericParameter(GenericOperatorParameter param) 
			throws ConversionException, AttributeNotFoundException {
		super.fromGenericParameter(param);
		
		if(! (param.getCaller() instanceof Agent) ) {
			throw conversionException("caller", Agent.class);
		}
		this.caller = (Agent)param.getCaller();
	}
	
	@Override
	public boolean equals(Object other) {
		if(!(other instanceof OperatorPluginParameter))
			return false;
		
		OperatorPluginParameter co = (OperatorPluginParameter)other;
		return super.equals(this) && co.caller == this.caller;
	}
	
	@Override
	public int hashCode() {
		return caller.hashCode() + 5;
	}

	@Override
	public OperatorStack getStack() {
		return caller;
	}
}
