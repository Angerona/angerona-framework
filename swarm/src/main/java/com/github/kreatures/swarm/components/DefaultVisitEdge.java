package com.github.kreatures.swarm.components;

import com.github.kreatures.core.BaseAgentComponent;
/**
 * This is the default implement of an visitEdge. a object of this class behaves all types of visitEdges. that means visit-, place- and timeEdge.
 * @author donfack
 *
 */
public class DefaultVisitEdge extends SwarmVisitEdgeGeneric {
	/**
	 * This constructor is use for make a copy of a object. 
	 * @param DefaultVisitEdge the object whose copy would be done. 
	 */
	public DefaultVisitEdge(DefaultVisitEdge edge){
		super(edge);
	}

	/**
	 * 
	 * @param agentName This is the name of agent which is allowing to visit a given station.
	 * @param stationName This is the station which would be visited by a given agent.
	 * @param bold  When this is true, then a agent has to visit the given station firstly.
	 */
	
	public DefaultVisitEdge(int id, String agentName, String stationName,boolean bold){
		super(id, agentName,  stationName, bold);
		//this.hashCode()
	}

	@Override
	public BaseAgentComponent clone() {
		return new DefaultVisitEdge(this);
	}
	@Override
	public int hashCode(){
		//TODO
		return this.getId();
	}

}
