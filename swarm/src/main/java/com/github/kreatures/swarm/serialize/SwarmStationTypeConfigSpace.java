package com.github.kreatures.swarm.serialize;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name="space")
public class SwarmStationTypeConfigSpace {
	@Attribute(name="value")
	protected int value;

}
