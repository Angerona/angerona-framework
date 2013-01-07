package angerona.fw.serialize;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;


/**
 * Contains the dynamic configurations of an agent saved in the agent
 * configuration files.
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
	@ElementList(inline=true, entry="operation-set", type=OperationSetConfigReal.class)
	protected List<OperationSetConfig> operators;
	
	@ElementList(name="components", entry="component", inline=true)
	protected List<String> componentClasses = new LinkedList<String>();
	
	@Override
	public List<OperationSetConfig> getOperations() {
		return operators;
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
