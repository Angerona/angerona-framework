package angerona.fw.serialize;

import java.io.File;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.core.Resolve;

/**
 * An implementation of the belief base configuration file as a reference of the form:
 * <source>filename.xml</source>. It is used by simple xml internally to load the
 * belief base configuration file.
 * @author Tim Janus
 */
@Root(name="beliefbase-config-import")
public class BeliefbaseConfigImport implements BeliefbaseConfig {

	@Element
	protected File source;

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

	@Override
	public OperationSetConfig getReasoners() {
		throw new IllegalStateException("Method not supported.");
	}

	@Override
	public OperationSetConfig getChangeOperators() {
		throw new IllegalStateException("Method not supported.");
	}

	@Override
	public OperationSetConfig getTranslators() {
		throw new IllegalStateException("Method not supported.");
	}
}
