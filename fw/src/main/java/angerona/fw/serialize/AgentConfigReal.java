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
public class AgentConfigReal implements AgentConfig{
	
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

	/** String identifying the Update Operator class name for dynamic instantiation */
	@Element(name="update-beliefs-class")
	private String updateBeliefsOperatorClass;

	/** String identifying the Planer class name for dynamic instantiation */
	@Element(name="subgoal-generation-class")
	private String subgoalGenerationClass;
	
	@ElementList(name="component-classes")
	private List<String> componentClasses = new LinkedList<String>();

	public static AgentConfigReal loadXml(File file) throws IOException {
		return SerializeHelper.loadXml(AgentConfigReal.class, file);
	}

	@Override
	public String getGenerateOptionsOperatorClass() {
		return generateOptionsOperatorClass;
	}

	@Override
	public String getIntentionUpdateOperatorClass() {
		return intentionUpdateOperatorClass;
	}

	@Override
	public String getViolatesOperatorClass() {
		return violatesOperatorClass;
	}

	@Override
	public String getUpdateOperatorClass() {
		return updateBeliefsOperatorClass;
	}

	@Override
	public String getSubgoalGenerationClass() {
		return subgoalGenerationClass;
	}
	
	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public List<String> getComponents() {
		return Collections.unmodifiableList(componentClasses);
	}
	
	public static void main(String [] args) {
		AgentConfigReal test = new AgentConfigReal();
		test.componentClasses.add("Knowhow");
		test.name = "AgentName";
		test.generateOptionsOperatorClass = "ggo";
		test.intentionUpdateOperatorClass = "iuo";
		test.subgoalGenerationClass = "sggo";
		test.updateBeliefsOperatorClass = "updbop";
		test.violatesOperatorClass = "violatesop";
		SerializeHelper.outputXml(test, System.out);
	}
}
