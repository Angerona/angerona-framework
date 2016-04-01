package com.github.kreatures.swarm.serialize;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name="visitEdge")
public class SwarmVisitEdgeConfig {
	@Attribute(name="id")
	protected int idSwarmVisitEdge;
	@Attribute(name="connectedIdRef1")
	protected int firstConnectedIdRefSwarmVisitEdge;
	@Attribute(name="connectedIdRef2")
	protected int secondConnectedIdRefSwarmVisitEdge;
	@Attribute (name="bold")
	protected String boldSwarmVisitEdge;
	
	public int getIdSwarmVisitEdge() {
		return idSwarmVisitEdge;
	}
	public int getFirstConnectedIdRefSwarmVisitEdge() {
		return firstConnectedIdRefSwarmVisitEdge;
	}
	public int getSecondConnectedIdRefSwarmVisitEdge() {
		return secondConnectedIdRefSwarmVisitEdge;
	}
	public String getBoldSwarmVisitEdge() {
		return boldSwarmVisitEdge;
	}
}
