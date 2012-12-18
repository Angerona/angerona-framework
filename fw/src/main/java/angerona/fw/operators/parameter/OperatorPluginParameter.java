package angerona.fw.operators.parameter;

import javax.management.AttributeNotFoundException;

import angerona.fw.Agent;
import angerona.fw.error.ConversionException;
import angerona.fw.operators.GenericOperatorParameter;

/**
 * Base class for all parameter for operators defined in the Operator-Plugin.
 * 
 * @author Tim Janus
 */
public abstract class OperatorPluginParameter implements OperatorParameter {

	/** the agent who calls the operator */
	private Agent caller;
	
	public OperatorPluginParameter() {}
	
	public OperatorPluginParameter(Agent owner) {
		this.caller = owner;
	}
	
	/** @return the agent who class the operator */
	public Agent getAgent() {
		return caller;
	}

	@Override
	public void fromGenericParameter(GenericOperatorParameter param) throws ConversionException {
		if(! (param.getCaller() instanceof Agent) ) {
			throw new ConversionException(GenericOperatorParameter.class, this.getClass(),
					new ClassCastException("Cannot cast owner to agent."));
		}
		this.caller = (Agent)param.getCaller();
	}
	

	protected void throwException(String name, Object obj, Class<?> castType)
		throws ConversionException{
		Exception inner = null;
		if(obj == null) {
			inner = new AttributeNotFoundException(
					"Parameter with name '" + name + "' not found.");
		} else {
			inner = new ClassCastException("Cannot convert '" + 
					name + "' to '" + castType.getName() + "'.");
		}
		throw new ConversionException(GenericOperatorParameter.class, 
				this.getClass(), inner);
	}
}
