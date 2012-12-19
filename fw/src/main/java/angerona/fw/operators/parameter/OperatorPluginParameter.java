package angerona.fw.operators.parameter;

import javax.management.AttributeNotFoundException;

import angerona.fw.Agent;
import angerona.fw.error.ConversionException;
import angerona.fw.internal.Entity;
import angerona.fw.operators.GenericOperatorParameter;
import angerona.fw.report.ReportPoster;
import angerona.fw.report.Reporter;

/**
 * Base class for all parameter for operators defined in the Operator-Plugin.
 * 
 * @author Tim Janus
 */
public abstract class OperatorPluginParameter 
	implements 
	OperatorParameter, 
	Reporter {

	/** the agent who calls the operator */
	private Agent caller;
	
	private ReportPoster operator;
	
	public OperatorPluginParameter() {}
	
	public OperatorPluginParameter(Agent caller, ReportPoster operator) {
		if(caller == null || operator == null) {
			throw new IllegalArgumentException("Both arguments most not be null.");
		}
		this.caller = caller;
		this.operator = operator;
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
	
	@Override
	public void visit(ReportPoster op) {
		operator = op;
	}
	
	@Override
	public void report(String message) {
		caller.report(message, operator);
	}
	
	@Override
	public void report(String message, Entity attachment) {
		caller.report(message, attachment, operator);
	}

	@Override
	public void report(String message, ReportPoster poster) {
		caller.report(message, poster);
	}
	
	@Override
	public void report(String message, Entity attachment, ReportPoster poster) {
		caller.report(message, attachment, poster);
	}
}
