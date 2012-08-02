package angerona.fw.serialize;

import java.io.File;
import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.core.Resolve;


@Root(name="agent-config-file")
public class AgentConfigImport implements AgentConfig {
	@Element
	private File source;

	@Resolve
	public AgentConfig substitute() throws Exception {
		return new Persister().read(AgentConfigReal.class, source);
	}
	
	@Override
	public OperatorSetConfig getGenerateOptionsOperators() {
		throw new IllegalStateException("Method not supported.");
	}

	@Override
	public OperatorSetConfig getIntentionUpdateOperators() {
		throw new IllegalStateException("Method not supported.");
	}

	@Override
	public OperatorSetConfig getViolatesOperators() {
		throw new IllegalStateException("Method not supported.");
	}

	@Override
	public OperatorSetConfig getUpdateOperators() {
		throw new IllegalStateException("Method not supported.");
	}

	@Override
	public OperatorSetConfig getSubgoalGenerators() {
		throw new IllegalStateException("Method not supported.");
	}

	@Override
	public String getName() {
		throw new IllegalStateException("Method not supported.");
	}
	
	@Override
	public List<String> getComponents() {
		throw new IllegalStateException("Method not supported.");
	}
	

	public static AgentConfigImport getTestObject()  {
		AgentConfigImport reval = new AgentConfigImport();
		reval.source = new File("config/skills/QueryAnswer.xml");
		return reval;
	}
}
