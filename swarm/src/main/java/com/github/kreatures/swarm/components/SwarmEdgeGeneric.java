/**
 * 
 */
package com.github.kreatures.swarm.components;

import com.github.kreatures.core.BaseAgentComponent;

/**
 * @author donfack
 * A Scenario can have more edges' type. The SwarmEdgeGeneric is the abstract class, 
 * which gives all common parameter between edge.
 */
public abstract class SwarmEdgeGeneric extends BaseAgentComponent implements SwarmEdge{
	/**
	 * This is the station's id where a agent is.
	 */
	protected int incommingStation;
	/**
	 * This is the station's id where the agent want to go.
	 */
	protected int outcommingStation;
	/**
	 * That is true when a edge has only one direction and false otherwise. If that is false,
	 * than incommingStation and outcommingStation mean the same things.  
	 */
	protected boolean underected=true;
	/**
	 * This gives the distance between the both stations.  
	 */
	protected int edgeLenght;
	
	/**
	 * The name of a edge has to be unique and no Null.
	 */
	protected String name;
	/**
	 * The Id of a edge has to be unique and strictly positive. 
	 */
	private int id;
	
	public int getIdEdge() {
		return id;
	}

	/**
	 * This constructor is use for make a copy of a object. 
	 * @param swarmEdgeGeneric 
	 */
public SwarmEdgeGeneric(SwarmEdgeGeneric swarmEdgeGeneric){
		super(swarmEdgeGeneric);
		this.incommingStation=swarmEdgeGeneric.incommingStation;
		this.outcommingStation=swarmEdgeGeneric.outcommingStation;
		this.edgeLenght=swarmEdgeGeneric.edgeLenght;
		this.underected=swarmEdgeGeneric.underected;
		this.id=swarmEdgeGeneric.id;
	}
	
	/**
	 * 
	 * @param incommingStation This is the station where a agent is.
	 * @param outcommingStation This is the station where the agent want to go.
	 * @param edgeLenght This gives the distance between the both stations.
	 * @param underected That is true when a edge has only one direction and false otherwise. If that is false,
	 * than incommingStation and outcommingStation mean the same things.
	 */
	
	public SwarmEdgeGeneric(int id, int incommingStation,int outcommingStation	,int edgeLenght, boolean underected){
		super();
		this.incommingStation=incommingStation;
		this.outcommingStation=outcommingStation;
		this.edgeLenght=edgeLenght;
		this.underected=underected;
		this.id=id;
	}
	/**
	 * This is the station's id where a agent is.
	 */
	public int getIncommingStation() {
		return incommingStation;
	}
	/**
	 * This is the station's id where the agent want to go.
	 */
	public int getOutcommingStation() {
		return outcommingStation;
	}
	/**
	 * That is true when a edge has only one direction and false otherwise. If that is false,
	 * than incommingStation and outcommingStation mean the same things.  
	 */
	public boolean isUnderected() {
		return underected;
	}
	/**
	 * This gives the distance between the both stations.  
	 */
	public int getEdgeLenght() {
		return edgeLenght;
	}
	/**
	 * The Id of a edge has to be unique and strictly positive. 
	 */
	public int getId() {
		return id;
	}
	
}
