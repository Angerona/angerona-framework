package angerona.fw.operators.parameter;

import javax.management.AttributeNotFoundException;

import angerona.fw.Agent;
import angerona.fw.error.ConversionException;
import angerona.fw.operators.GenericOperatorParameter;
import angerona.fw.operators.OperatorStack;

/**
 * Base class for all parameter for operators defined in the Operator-Plugin.
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
	public OperatorStack getCaller() {
		return caller;
	}	
	
	/** @return the agent who class the operator */
	public Agent getAgent() {
		return caller;
	}

	@Override
	public void fromGenericParameter(GenericOperatorParameter param) 
			throws ConversionException, AttributeNotFoundException {
		if(! (param.getCaller() instanceof Agent) ) {
			throw conversionException("caller", Agent.class);
		}
		this.caller = (Agent)param.getCaller();
	}
}
