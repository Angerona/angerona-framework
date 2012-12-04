package angerona.fw.operators;

import java.util.HashMap;
import java.util.Map;

import javax.management.AttributeNotFoundException;

public class GenericOperatorParameter {
	private Object owner;
	
	private Map<String, Object> parameters = new HashMap<String, Object>();
	
	public GenericOperatorParameter() {
	}
	
	public GenericOperatorParameter(Object owner) {
		this.owner = owner;
	}
	
	public Object getOwner() {
		return owner;
	}
	
	public void setParameter(String name, Object value) {
		parameters.put(name, value);
	}
	
	public Object getParameterRequired(String name) 
		throws AttributeNotFoundException {
		Object reval = getParameter(name);
		if(reval == null)
			throw new AttributeNotFoundException("Cannot find parameter '"+name+"'.");
		return reval;
	}
	
	public Object getParameter(String name) {
		if(parameters.containsKey(name)) {
			return parameters.get(name);
		}
		return null;
	}
}
