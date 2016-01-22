package com.github.kreatures.swarm.serialize;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
@Root(name="placeEdge")
public class SwarmPlaceEdgeConfig {
	
	@Attribute(name="id")
	protected int idSwarmPlaceEdge;
	@Attribute(name="connectedIdRef1")
	protected int firstConnectedIdRefSwarmPlaceEdge;
	@Attribute(name="connectedIdRef2")
	protected int secondConnectedIdRefSwarmPlaceEdge;
	@Attribute (name="value")
	protected int valueSwarmPlaceEdge;
	@Attribute(name="directed")
	protected String directedSwarmPlaceEdge;
	
	
	public int getIdSwarmPlaceEdge() {
		return idSwarmPlaceEdge;
	}
	public int getFirstConnectedIdRefSwarmPlaceEdge() {
		return firstConnectedIdRefSwarmPlaceEdge;
	}
	public int getSecondConnectedIdRefSwarmPlaceEdge() {
		return secondConnectedIdRefSwarmPlaceEdge;
	}
	public int getValueSwarmPlaceEdge() {
		return valueSwarmPlaceEdge;
	}
	public String getDirectedSwarmPlaceEdge() {
		return directedSwarmPlaceEdge;
	}
	
	
}
