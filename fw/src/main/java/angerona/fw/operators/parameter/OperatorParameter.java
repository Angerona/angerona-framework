package angerona.fw.operators.parameter;

import angerona.fw.error.ConversionException;
import angerona.fw.operators.GenericOperatorParameter;

/**
 * 
 * @author Tim Janus
 */
public interface OperatorParameter {
	 
	/** 
	 * This method converts the given GenericOperatorParameter into a more
	 * user friendly version which has type information.
	 * The information is saved in the object instance which implements the
	 * OperatorParameter interface.
	 * @param input		The generic operator paraemter data structure
	 * @throws ConversionException
	 */
	void fromGenericParameter(GenericOperatorParameter input) throws ConversionException;
}
