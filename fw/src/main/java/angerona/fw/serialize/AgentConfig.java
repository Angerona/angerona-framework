package angerona.fw.serialize;

import java.util.List;

/**
 * 
 * @author Tim Janus
 */
public interface AgentConfig {
	/** @return String identifying the GenerateOptions Operator class name */
	public String getGenerateOptionsOperatorClass();
	
	/** @return String identifying the Filter Operator class name */
	public String getIntentionUpdateOperatorClass();

	/** @return String identifying the Violates Operator class name */
	public String getViolatesOperatorClass();

	/** @return String identifying the Policy-Control Operator class name */
	public String getPolicyControlOperatorClass();

	/** @return String identifying the Update Operator class name */
	public String getUpdateOperatorClass();

	/** @return String identifying the Planer Operator class name */
	public String getSubgoalGenerationClass();
	
	/** @return String with name of the agent configuration */
	public String getName();
	
	/** @return list of class names of agent-components */
	public List<String> getComponents();
}
