package com.github.angerona.fw.serialize;

import java.util.List;

import com.github.angerona.fw.asml.CommandSequence;

/**
 * Interface for serializable simple xml classes of the agent configuration file.
 * It contains all information saved in an agent configuration file. This are the
 * name of the configuration. The List of operation sets the agent can use and the list
 * of components (identified by full java class name) the agent instantiates at run-
 * time.
 * 
 * @author Tim Janus
 */
public interface AgentConfig extends Resource {
	public static final String RESOURCE_TYPE = "Agent-Configuration";
	
	/** @return String identifying the GenerateOptions Operator class name */
	public List<OperationSetConfig> getOperations();
	
	/** @return String with name of the agent configuration */
	public String getName();
	
	/** @return the agent's cylce script as a commando sequence */
	public CommandSequence getCycleScript();
	
	/** @return list of class names of agent-components */
	public List<String> getComponents();
}
