package angerona.fw.serialize;

import java.io.File;
import java.util.List;
import java.util.Set;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.core.Resolve;


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
	public Set<OperationSetConfig> getOperations() {
		throw new IllegalStateException("Method not supported.");
	}
}
