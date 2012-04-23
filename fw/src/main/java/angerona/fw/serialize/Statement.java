package angerona.fw.serialize;

import java.util.HashMap;
import java.util.Map;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A statement of a Skill. It consist of a name of the commando
 * a name for the return value and a map of parameter names.
 * @author Tim Janus
 */
@Root(name="statement")
public class Statement {
	/** reference to the logback logger instance */
	private Logger LOG = LoggerFactory.getLogger(Statement.class);
	
	/** The name of the commando */
	@Element(name="name")
	private String name;
	
	/** a map from parameter names to object names in the context */
	@ElementMap(entry="param", key="key", name="parameters", attribute=true, inline=true, required=false, empty=false)
	private Map<String, String> parameterMap = new HashMap<String, String>();
	
	/** the future name of the return value of this commando in the context */
	@Element(name="return-value-identifier")
	private String returnValueIdentifier;

	/** an inner xml element for complex commandos like sendAction */
	@Element(name="complex-info", required=false)
	private Object complexInfo;
	
	/** @return the name of the commando */
	public String getCommandoName() {
		return name;
	}

	public String getParameter(String param) {
		if(parameterMap.containsKey(param)) {
			return parameterMap.get(param);
		} else {
			LOG.warn("Statement '{}': has no parameter '{}'", name, param);
			return null;
		}
	}
	
	/** @return the future name of the return value of this commando in the context */
	public String getReturnValueIdentifier() {
		return returnValueIdentifier;
	}
	
	public Object getComplexInfo() {
		return complexInfo;
	}
	
	public static Statement getTestObject() {
		Statement test = new Statement();
		test.name = "reason";
		test.parameterMap.put("sentence", "$world");
		test.parameterMap.put("beliefbase", "$in.question");
		test.returnValueIdentifier = "answer";
		return test;
	}
}
