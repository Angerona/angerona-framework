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
	public String getGenerateOptionsOperatorClass() {
		throw new IllegalStateException("Method not supported.");
	}

	@Override
	public String getIntentionUpdateOperatorClass() {
		throw new IllegalStateException("Method not supported.");
	}

	@Override
	public String getViolatesOperatorClass() {
		throw new IllegalStateException("Method not supported.");
	}

	@Override
	public String getPolicyControlOperatorClass() {
		throw new IllegalStateException("Method not supported.");
	}

	@Override
	public String getUpdateOperatorClass() {
		throw new IllegalStateException("Method not supported.");
	}

	@Override
	public String getSubgoalGenerationClass() {
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
