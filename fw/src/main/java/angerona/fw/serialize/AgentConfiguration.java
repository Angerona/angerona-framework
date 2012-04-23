package angerona.fw.serialize;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

/**
 * Contains the dynamic configurations of an agent.
 * It holds the type names of the classes use for different operators.
 * 
 * @author Tim Janus
 */
@Root(name="agent-configuration")
public class AgentConfiguration {
	
	/** String with name of this agent configuration */
	@Element
	private String name;
	
	/** String identifying the GenerateOptions Operator class name for dynamic instantiation */
	@Element(name="generate-options-class")
	private String generateOptionsOperatorClass;

	/** String identifying the Filter Operator class name for dynamic instantiation */
	@Element(name="intention-update-class")
	private String intentionUpdateOperatorClass;

	/** String identifying the Violates Operator class name for dynamic instantiation */
	@Element(name="violates-class")
	private String violatesOperatorClass;

	/** String identifying the Policy-Control Operator class name for dynamic instantiation */
	@Element(name="policy-control-class")
	private String policyControlOperatorClass;

	/** String identifying the Update Operator class name for dynamic instantiation */
	@Element(name="update-beliefs-class")
	private String updateBeliefsOperatorClass;

	/** String identifying the Planer class name for dynamic instantiation */
	@Element(name="subgoal-generation-class")
	private String subgoalGenerationClass;
	
	@ElementList(name="component-classes")
	private List<String> componentClasses = new LinkedList<String>();

	public static AgentConfiguration loadXml(File file) throws IOException {
		return SerializeHelper.loadXml(AgentConfiguration.class, file);
	}

	/** @return String identifying the GenerateOptions Operator class name */
	public String getGenerateOptionsOperatorClass() {
		return generateOptionsOperatorClass;
	}

	/** @return String identifying the Filter Operator class name */
	public String getIntentionUpdateOperatorClass() {
		return intentionUpdateOperatorClass;
	}

	/** @return String identifying the Violates Operator class name */
	public String getViolatesOperatorClass() {
		return violatesOperatorClass;
	}

	/** @return String identifying the Policy-Control Operator class name */
	public String getPolicyControlOperatorClass() {
		return policyControlOperatorClass;
	}

	/** @return String identifying the Update Operator class name */
	public String getUpdateOperatorClass() {
		return updateBeliefsOperatorClass;
	}

	/** @return String identifying the Planer Operator class name */
	public String getSubgoalGenerationClass() {
		return subgoalGenerationClass;
	}
	
	/** @return String with name of the agent configuration */
	public String getName() {
		return name;
	}
	
	public List<String> getComponents() {
		return Collections.unmodifiableList(componentClasses);
	}
	
	public static void main(String [] args) {
		AgentConfiguration test = new AgentConfiguration();
		test.componentClasses.add("Knowhow");
		test.name = "AgentName";
		test.generateOptionsOperatorClass = "ggo";
		test.intentionUpdateOperatorClass = "iuo";
		test.policyControlOperatorClass = "pco";
		test.subgoalGenerationClass = "sggo";
		test.updateBeliefsOperatorClass = "updbop";
		test.violatesOperatorClass = "violatesop";
		SerializeHelper.outputXml(test, System.out);
	}
}
