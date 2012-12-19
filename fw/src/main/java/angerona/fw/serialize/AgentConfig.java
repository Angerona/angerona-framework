package angerona.fw.serialize;

import java.util.List;

/**
 * 
 * @author Tim Janus
 */
public interface AgentConfig {
	/** @return String identifying the GenerateOptions Operator class name */
	public List<OperationSetConfig> getOperations();
	
	/** @return String with name of the agent configuration */
	public String getName();
	
	/** @return list of class names of agent-components */
	public List<String> getComponents();
}
