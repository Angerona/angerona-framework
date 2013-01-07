package angerona.fw.serialize;

import java.io.File;
import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.core.Resolve;

/**
 * An implementation of the agent configuration file as a reference of the form:
 * <source>filename.xml</source>. It is used by simple xml internally to load the
 * agent configuration file.
 * @author Tim Janus
 */
@Root(name="agent-config-file")
public class AgentConfigImport implements AgentConfig {
	@Element
	protected File source;

	@Resolve
	public AgentConfig substitute() throws Exception {
		return new Persister().read(AgentConfigReal.class, source);
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
}
