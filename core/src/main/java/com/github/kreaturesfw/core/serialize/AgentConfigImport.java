package com.github.kreaturesfw.core.serialize;

import java.io.File;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Resolve;

import com.github.kreaturesfw.core.asml.CommandSequence;

/**
 * An implementation of the agent configuration file as a reference to another file.
 * It uses the following form:
 * 
 *     <agent-config source="filename.xml" />
 * 
 * It is used by simple xml internally to load the
 * agent configuration file.
 * @author Tim Janus
 */
@Root(name="agent-config")
public class AgentConfigImport implements AgentConfig {
	@Attribute(name="source")
	protected File source;

	@Resolve
	public AgentConfig substitute() throws Exception {
		return SerializeHelper.get().loadXml(AgentConfigReal.class, source);
	}

	@Override
	public String getName() {
		throw new IllegalStateException("Method not supported.");
	}
	
	@Override
	public List<String> getComponents() {
		throw new IllegalStateException("Method not supported.");
	}

	@Override
	public List<OperationSetConfig> getOperations() {
		throw new IllegalStateException("Method not supported.");
	}

	@Override
	public CommandSequence getCycleScript() {
		throw new IllegalStateException("Method not supported.");
	}

	@Override
	public String getDescription() {
		throw new IllegalStateException("Method not supported.");
	}

	@Override
	public String getResourceType() {
		throw new IllegalStateException("Method not supported.");
	}

	@Override
	public String getCategory() {
		throw new IllegalStateException("Method not supported.");
	}
}
