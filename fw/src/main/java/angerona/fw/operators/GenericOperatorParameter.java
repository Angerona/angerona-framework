package angerona.fw.operators;

import java.util.HashMap;
import java.util.Map;

import javax.management.AttributeNotFoundException;

/**
 * Implements a generic operator parameter wrapper by using a String to Object
 * map to represent the parameter. It is the task of the operators to translate the
 * GenericOperatorParameter object into a specialized object and to test if all 
 * the required parameters are given.
 * @author Tim Janus
 */
public class GenericOperatorParameter {
	/** The caller of the operator */
	private OperatorStack caller;
	
	/** The parameter map */
	private Map<String, Object> parameters = new HashMap<String, Object>();
	
	/**
	 * CTor: Requres the caller as parameter
	 * @param caller	reference to the caller of an operator.
	 */
	public GenericOperatorParameter(OperatorStack caller) {
		this.caller = caller;
	}
	
	/** @return the caller of an operator */
	public OperatorStack getCaller() {
		return caller;
	}
	
	/**
	 * Sets the given vales as parameter value using the given name.
	 * @param name	The name of the parameter to set
	 * @param value	The object representing the new value of the parameter.
	 */
	public void setParameter(String name, Object value) {
		parameters.put(name, value);
	}
	
	/**
	 * Gets the parameter with the given name. If the parameter does not
	 * exists the method throws an AttributeNotFoundException.
	 * @param name	The name of the parameter which shall be returned.
	 * @return	The reference to the object representing the parameter.
	 * @throws AttributeNotFoundException
	 */
	public Object getParameterRequired(String name) 
		throws AttributeNotFoundException {
		Object reval = getParameter(name);
		if(reval == null)
			throw new AttributeNotFoundException("Cannot find parameter '"+name+"'.");
		return reval;
	}
	
	/**
	 * Gets the parameter with the given name.
	 * @param name	The name of the parameter which shall be returned.
	 * @return	An object representing the value of the parameter with the given name
	 * 			or null if the parameter with the given name does not exist.
	 */
	public Object getParameter(String name) {
		if(parameters.containsKey(name)) {
			return parameters.get(name);
		}
		return null;
	}
}
