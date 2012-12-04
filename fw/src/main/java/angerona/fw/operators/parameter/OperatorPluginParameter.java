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
	private Agent owner;
	
	public OperatorPluginParameter() {}
	
	public OperatorPluginParameter(Agent owner) {
		this.owner = owner;
	}
	
	/** @return the agent who class the operator */
	public Agent getAgent() {
		return owner;
	}

	@Override
	public void fromGenericParameter(GenericOperatorParameter param) throws ConversionException {
		if(! (param.getOwner() instanceof Agent) ) {
			throw new ConversionException(GenericOperatorParameter.class, this.getClass(),
					new ClassCastException("Cannot cast owner to agent."));
		}
		this.owner = (Agent)param.getOwner();
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
