package angerona.fw.serialize;

import java.util.HashSet;
import java.util.Set;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;

public class OperatorSetConfigReal implements OperatorSetConfig {

	@Element(name="default-operator-cls")
	private String defaultClassName;
	
	@ElementList(required=false, inline=true, entry="operator-cls", empty=false)
	private Set<String> operatorClassNames = new HashSet<String>();
	
	@Override
	public String getDefaultClassName() {
		return defaultClassName;
	}

	@Override
	public Set<String> getOperatorClassNames() {
		return operatorClassNames;
	}

	public static OperatorSetConfig getExample(String prefix) {
		OperatorSetConfigReal reval = new OperatorSetConfigReal();
		reval.defaultClassName = prefix+"-default";
		reval.operatorClassNames.add(prefix+"-default");
		reval.operatorClassNames.add(prefix+"-alternative1");
		return reval;
	}
}
