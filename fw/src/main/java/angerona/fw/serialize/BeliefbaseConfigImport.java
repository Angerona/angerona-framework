package angerona.fw.serialize;

import java.io.File;
import java.util.Set;

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
	public Set<String> getRevisionClassName() {
		throw new IllegalStateException("Method not supported.");
	}

	@Override
	public Set<String> getReasonerClassName() {
		throw new IllegalStateException("Method not supported.");
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
	public String getDefaultReasonerClass() {
		throw new IllegalStateException("Method not supported.");
	}

	@Override
	public String getDefaultChangeClass() {
		throw new IllegalStateException("Method not supported.");
	}
}
