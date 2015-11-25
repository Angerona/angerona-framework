package com.github.kreaturesfw.core.serialize;

import java.util.HashSet;
import java.util.Set;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name="operation-set")
public class OperationSetConfigReal implements OperationSetConfig {

	@Attribute(name="operation-type", required=false)
	protected String operationType;
	
	@Element(name="default-operator-cls")
	protected String defaultClassName;
	
	@ElementList(required=false, inline=true, entry="operator-cls", empty=false)
	protected Set<String> operatorClassNames = new HashSet<String>();
	
	@Override
	public String getDefaultClassName() {
		return defaultClassName;
	}

	@Override
	public Set<String> getOperatorClassNames() {
		return operatorClassNames;
	}

	@Override
	public String getOperationType() {
		return operationType;
	}
}
