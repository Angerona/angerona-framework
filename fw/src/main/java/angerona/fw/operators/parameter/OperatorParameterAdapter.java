package angerona.fw.operators.parameter;

import java.util.HashMap;
import java.util.Map;

import javax.management.AttributeNotFoundException;

import angerona.fw.error.ConversionException;
import angerona.fw.internal.Entity;
import angerona.fw.operators.GenericOperatorParameter;
import angerona.fw.report.Reporter;

/**
 * Abstract base class for all input-parameter implementations 
 * for the operators. The class implements the interface between
 * the operator and the Angerona report mechanism.
 * 
 * The visitor pattern is used to inform the reporter interface about
 * the current default report poster.
 * 
 * @author Tim Janus
 */
public abstract class OperatorParameterAdapter implements 
	OperatorParameter,
	Reporter
	{

	private Map<String, String> settings = new HashMap<String, String>();
	
	@Override
	public void fromGenericParameter(GenericOperatorParameter input) 
			throws ConversionException, AttributeNotFoundException {
		settings = input.getSettings();
	}
	
	@Override
	public void report(String message) {
		getReporter().report(message);
	}
	
	@Override
	public void report(String message, Entity attachment) {
		getReporter().report(message, attachment);
	}
	
	public String getSetting(String name, String def) {
		if(!this.settings.containsKey(name)) {
			return def;
		} else {
			return this.settings.get(name);
		}
	}
	
	/**
	 * Generates a ConversionException when converting between GenericOperatorParameter and a 
	 * class implementing OperatorParameter. The name of the parameter causing the exception and
	 * the type which is used to cast the parameter is given to form an accurate error message.
	 * @param name		The name of the parameter which caused the exception
	 * @param castType	The type used to cast the parameter object.
	 * @return			An ConversionException object represnting the error.
	 */
	protected ConversionException conversionException(
			String name, Class<?> castType) {
		
		Exception  inner = new ClassCastException("Cannot convert '" + 
				name + "' to '" + castType.getName() + "'.");
		return new ConversionException(GenericOperatorParameter.class, 
				this.getClass(), inner);
	}
}
