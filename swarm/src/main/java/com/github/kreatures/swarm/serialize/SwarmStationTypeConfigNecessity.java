package com.github.kreatures.swarm.serialize;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name="necessity")
public class SwarmStationTypeConfigNecessity {
	@Attribute(name="value")
	protected boolean value;
}
