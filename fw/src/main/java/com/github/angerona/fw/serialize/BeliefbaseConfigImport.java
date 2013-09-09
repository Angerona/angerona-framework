package com.github.angerona.fw.serialize;

import java.io.File;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Resolve;

/**
 * An implementation of the belief base configuration file as a reference to another file.
 * It uses the following form:
 * 
 *     <beliefbase-config source="filename.xml" />
 *     
 * It is used by simple xml internally to load the
 * belief base configuration file.
 * 
 * @author Tim Janus
 */
@Root(name="beliefbase-config")
public class BeliefbaseConfigImport implements BeliefbaseConfig {

	@Attribute(name="source")
	protected File source;

	@Resolve
	public BeliefbaseConfig substitute() throws Exception {
		return SerializeHelper.loadXml(BeliefbaseConfigReal.class, source);
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

	@Override
	public String getViewOn() {
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

	@Override
	public BeliefOperatorFamilyConfig getBeliefOperatorFamily() {
		throw new IllegalStateException("Method not supported.");
	}
}
