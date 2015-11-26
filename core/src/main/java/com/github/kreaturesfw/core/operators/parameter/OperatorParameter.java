package com.github.kreaturesfw.core.operators.parameter;

import javax.management.AttributeNotFoundException;

import com.github.kreaturesfw.core.error.ConversionException;
import com.github.kreaturesfw.core.legacy.SettingsStorage;
import com.github.kreaturesfw.core.operators.OperatorCaller;

/**
 * classes implementing this interface act as input parameters for the generic Operator
 * process methods. The OperatorParameter provides the functionality given by the
 * OperatorCaller to the Operator, this means the operator can access the call stack to
 * push and pop itself on the stack and it can access the reporter to access the Angerona
 * report system.
 * 
 * @see Operator
 * @author Tim Janus
 */
public interface OperatorParameter extends OperatorCaller, SettingsStorage {
	
	/** 
	 * This method converts the given GenericOperatorParameter into a more
	 * user friendly version which has type information.
	 * The information is saved in the object instance which implements the
	 * OperatorParameter interface.
	 * @param input		The generic operator parameter data structure
	 * @throws ConversionException
	 */
	void fromGenericParameter(GenericOperatorParameter input) 
			throws ConversionException, AttributeNotFoundException;
}
