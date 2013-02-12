package angerona.fw.operators.parameter;

import javax.management.AttributeNotFoundException;

import angerona.fw.error.ConversionException;
import angerona.fw.internal.Entity;
import angerona.fw.operators.GenericOperatorParameter;
import angerona.fw.report.FullReporter;
import angerona.fw.report.ReportPoster;
import angerona.fw.report.Reporter;

public abstract class OperatorParameterAdapter implements 
	OperatorParameter,
	Reporter
	{

	private FullReporter reporter;
	
	@Override
	public abstract void fromGenericParameter(GenericOperatorParameter input) 
			throws ConversionException, AttributeNotFoundException;
	
	@Override
	public void visit(ReportPoster op) {
		reporter = getCaller().getReporter();
		reporter.setDefaultPoster(op);
	}
	
	public void report(String message) {
		reporter.report(message);
	}
	
	public void report(String message, Entity attachment) {
		reporter.report(message, attachment);
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
