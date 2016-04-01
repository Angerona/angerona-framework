package com.github.kreatures.swarm.serialize;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name="cycle")
public class SwarmConfigCycle {
	@Attribute(name="value")
	protected int value;

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
}