package angerona.fw.serialize;

import java.io.File;
import java.io.IOException;
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
	protected String name;
	
	/** String identifying the GenerateOptions Operator-Set for dynamic instantiation */
	@Element(name="generate-options-operators")
	protected OperatorSetConfig generateOptionsOperators;
	
	/** String identifying the Filter Operator-Set for dynamic instantiation */
	@Element(name="intention-update-operators")
	protected OperatorSetConfig intentionUpdateOperators;
	
	/** String identifying the Violates Operator-Set for dynamic instantiation */
	@Element(name="violates-operators")
	protected OperatorSetConfig violatesOperators;

	/** String identifying the Update Operator-Set for dynamic instantiation */
	@Element(name="update-beliefs-operators")
	protected OperatorSetConfig updateBeliefsOperators;

	/** String identifying the Planer-Set for dynamic instantiation */
	@Element(name="subgoal-generation-operators")
	protected OperatorSetConfig subgoalGenerationOperators;
	
	@ElementList(name="components", entry="component", inline=true)
	protected List<String> componentClasses = new LinkedList<String>();

	@Override
	public OperatorSetConfig getGenerateOptionsOperators() {
		return generateOptionsOperators;
	}

	@Override
	public OperatorSetConfig getIntentionUpdateOperators() {
		return intentionUpdateOperators;
	}

	@Override
	public OperatorSetConfig getViolatesOperators() {
		return violatesOperators;
	}

	@Override
	public OperatorSetConfig getUpdateOperators() {
		return updateBeliefsOperators;
	}

	@Override
	public OperatorSetConfig getSubgoalGenerators() {
		return subgoalGenerationOperators;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public List<String> getComponents() {
		return componentClasses;
	}
	
	public static AgentConfigReal loadXml(File file) throws IOException {
		return SerializeHelper.loadXml(AgentConfigReal.class, file);
	}
}
