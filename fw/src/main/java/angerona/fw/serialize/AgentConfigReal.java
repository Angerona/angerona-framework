package angerona.fw.serialize;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.PersistenceException;
import org.simpleframework.xml.core.Validate;

import angerona.fw.asml.CommandSequence;


/**
 * Contains the dynamic configurations of an agent saved in the agent
 * configuration files.
 * It holds the type names of the classes use for different operators.
 * 
 * @author Tim Janus
 */
@Root(name="agent-configuration")
public class AgentConfigReal implements AgentConfig {
	
	/** String with name of this agent configuration */
	@Element
	protected String name;
	
	/** the link to the ASML script used to execute the agent's cycle. */
	@Element(name="cycle-script", type=CommandSequenceSerializeImport.class)
	protected CommandSequenceSerialize cylceScript;
	
	/** String identifying the GenerateOptions Operator-Set for dynamic instantiation */
	@ElementList(inline=true, entry="operation-set", type=OperationSetConfigReal.class)
	protected List<OperationSetConfig> operators;
	
	@ElementList(name="components", entry="component", inline=true)
	protected List<String> componentClasses = new LinkedList<String>();
	
	@Element(name="description", required=false)
	protected String description = "";
	
	@Element(name="category", required=false)
	protected String category = "";
	
	@Override
	public List<OperationSetConfig> getOperations() {
		return operators;
	}

	@Override
	public String getName() {
		return name;
	}

	public CommandSequence getCycleScript() {
		return (CommandSequence)this.cylceScript;
	}
	
	@Override
	public List<String> getComponents() {
		return componentClasses;
	}
	
	/**
	 * Checks if the loaded agent instance is valid.
	 * @throws PersistenceException
	 */
	@Validate
	public void validation() throws PersistenceException {
		if(!(this.cylceScript instanceof CommandSequence)) {
			throw new PersistenceException("cycle-script is not of type '%s' but of type '%s", 
					CommandSequence.class.getName(), 
					this.cylceScript == null ? "null" : this.cylceScript.getClass().getName());
		}
	}
	
	public static AgentConfigReal loadXml(File file) throws IOException {
		return SerializeHelper.loadXml(AgentConfigReal.class, file);
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public String getResourceType() {
		return RESOURCE_TYPE;
	}

	@Override
	public String getCategory() {
		return category;
	}
}
