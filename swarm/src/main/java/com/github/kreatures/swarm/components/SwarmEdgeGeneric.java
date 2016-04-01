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
	 * This is the station's name where a agent is.
	 */
	protected String outgoingStation;
	/**
	 * This is the station's name where the agent want to go.
	 */
	protected String incomingStation;
	/**
	 * That is true when a edge has only one direction and false otherwise. If that is false,
	 * then incommingStation and outcommingStation mean the same things.  
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
		this.incomingStation=swarmEdgeGeneric.incomingStation;
		this.outgoingStation=swarmEdgeGeneric.outgoingStation;
		this.edgeLenght=swarmEdgeGeneric.edgeLenght;
		this.underected=swarmEdgeGeneric.underected;
		this.id=swarmEdgeGeneric.id;
		setName();
	}
	
	/**
	 * 
	 * @param outgoingStation This is the station where a agent is.
	 * @param incomingStation This is the station where the agent want to go.
	 * @param edgeLenght This gives the distance between the both stations.
	 * @param underected That is true when a edge has only one direction and false otherwise. If that is false,
	 * then incommingStation and outgoingStation mean the same things.
	 */
	
	public SwarmEdgeGeneric(int id, String incomingStation,String outgoingStation,int edgeLenght, boolean underected){
		super();
		this.incomingStation=incomingStation;
		this.outgoingStation=outgoingStation;
		this.edgeLenght=edgeLenght;
		this.underected=underected;
		this.id=id;
		setName();
	}
	/**
	 * This defines the name of a placed edge as a unique key and it is a String.  
	 */
	private void setName(){
		if(outgoingStation!=null && incomingStation!=null ){
			this.name = outgoingStation+";"+incomingStation;
		}
	}
	/**
	 * The Table has two elements. The first is the  outgoingStation's name
	 * and the second is the incomingStation's name.
	 * @return the table as name as key of this placed edge object.
	 */
	public String[] getTableName(){
		return name.split(";");
	}
	/**
	 * @return the table as name as key of this placed edge object.
	 */
	public String getName(){
		return name;
	}
	
	/**
	 * This is the station's name where the agent want to go.
	 */
	public String getIncomingStation() {
		return incomingStation;
	}
	/**
	 * This is the station's name where a agent is.
	 */
	public String getOutgoingStation() {
		return outgoingStation;
	}
	/**
	 * That is true when a edge has only one direction and false otherwise. If that is false,
	 * then incomingStation and outgoingStation mean the same things.  
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
