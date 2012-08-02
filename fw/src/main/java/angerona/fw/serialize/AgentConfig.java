package angerona.fw.serialize;

import java.util.List;

/**
 * 
 * @author Tim Janus
 */
public interface AgentConfig {
	/** @return String identifying the GenerateOptions Operator class name */
	public OperatorSetConfig getGenerateOptionsOperators();
	
	/** @return String identifying the Filter Operator class name */
	public OperatorSetConfig getIntentionUpdateOperators();
	
	/** @return String identifying the Violates Operator class name */
	public OperatorSetConfig getViolatesOperators();
	

	/** @return String identifying the Update Operator class name */
	public OperatorSetConfig getUpdateOperators();
	
	/** @return String identifying the Planer Operator class name */
	public OperatorSetConfig getSubgoalGenerators();
	
	/** @return String with name of the agent configuration */
	public String getName();
	
	/** @return list of class names of agent-components */
	public List<String> getComponents();
}
