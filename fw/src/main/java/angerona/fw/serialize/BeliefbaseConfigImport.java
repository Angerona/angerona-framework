package angerona.fw.serialize;

import java.io.File;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.core.Resolve;


@Root(name="beliefbase-config-import")
public class BeliefbaseConfigImport implements BeliefbaseConfig {

	@Element
	private File source;

	@Resolve
	public BeliefbaseConfig substitute() throws Exception {
		return new Persister().read(BeliefbaseConfigReal.class, source);
	}
	
	@Override
	public String getBeliefbaseClassName() {
		throw new IllegalStateException("Method not supported.");
	}

	@Override
	public String getName() {
		throw new IllegalStateException("Method not supported.");
	}
	
	public static BeliefbaseConfigImport getTestObject()  {
		BeliefbaseConfigImport reval = new BeliefbaseConfigImport();
		reval.source = new File("config/skills/QueryAnswer.xml");
		return reval;
	}

	@Override
	public OperatorSetConfig getReasoners() {
		throw new IllegalStateException("Method not supported.");
	}

	@Override
	public OperatorSetConfig getChangeOperators() {
		throw new IllegalStateException("Method not supported.");
	}

	@Override
	public OperatorSetConfig getTranslators() {
		throw new IllegalStateException("Method not supported.");
	}
}
