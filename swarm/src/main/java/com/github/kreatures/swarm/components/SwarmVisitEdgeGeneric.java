/**
 * 
 */
package com.github.kreatures.swarm.components;

import com.github.kreatures.core.BaseAgentComponent;

/**
 * @author donfack
 * A Scenario can have more edges' type. The SwarmVisitEdgeGeneric is the abstract class, 
 * which gives all common parameter between visitEdge.
 */
public abstract class SwarmVisitEdgeGeneric extends BaseAgentComponent implements SwarmEdge{
	/**
	 * That is the name of agent which allowing to visit a station
	 */
	protected String agentName;
	
	/**
	 * That is the name of station which would be visited by a agent.
	 */
	protected String stationName;
	
	/**
	 * When this is true, then a agent has to visit the given station firstly.
	 *   
	 */
	protected boolean bold=false;

	/**
	 * The Id of a visitEdge has to be unique and strictly positive. 
	 */
	private int id;
	
	public int getIdEdge() {
		return id;
	}

	/**
	 * This constructor is use for make a copy of a object. 
	 * @param swarmVisitEdgeGeneric the object whose copy would be done. 
	 */
	public SwarmVisitEdgeGeneric(SwarmVisitEdgeGeneric swarmVisitEdgeGeneric){
		super(swarmVisitEdgeGeneric);
		this.agentName=swarmVisitEdgeGeneric.agentName ;
		this.stationName=swarmVisitEdgeGeneric.stationName ;
		this.bold=swarmVisitEdgeGeneric.bold ;
		this.id=swarmVisitEdgeGeneric.id;
	}
	
	/**
	 * 
	 * @param agentName This is the name of agent which is allowing to visit a given station.
	 * @param stationName This is the station which would be visited by a given agent.
	 * @param bold  When this is true, then a agent has to visit the given station firstly.
	 */
	
	public SwarmVisitEdgeGeneric(int id, String agentName, String stationName,boolean bold){
		super();
		this.agentName=agentName;
		this.stationName=stationName;
		this.bold=bold;
		this.id=id;
	}
	/**
	 * That is the name of agent which allowing to visit a station
	 */
	public String getAgentName() {
		return agentName;
	}
	/**
	 * That is the name of station which would be visited by a agent.
	 * @return
	 */
	public String getStationName() {
		return stationName;
	}
	/**
	 * When this is true, then a agent has to visit the given station firstly.
	 * @return
	 */
	public boolean isBold() {
		return bold;
	}

	public int getId() {
		return id;
	}

	
}
