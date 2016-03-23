package com.github.kreatures.swarm.serialize;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name="item")
public class SwarmStationTypeConfigItem {
	@Attribute(name="value")
	protected int value;

	public int getValue() {
		return value;
	}

}
