package com.github.kreatures.swarm.components;

import java.util.Hashtable;


import com.github.kreatures.core.BaseAgentComponent;

/**
 * 
 * @author Donfack
 *This Class SwarmStationGeneric is a station's generic. All station's types have to extends this class. 
 */
public abstract class SwarmStationGeneric  extends BaseAgentComponent implements SwarmStation {
	/**
	 * This is station's id.
	 */
	private int stationId;
	/**
	 * This is number of places where agents can work at the same time into the station.
	 * Per default, that is one place.
	 */
	protected int space=1;
	
	/**
	 * This is the number of stations, whose have these characteristics.
	 */
	protected int count=1;
	
	/**
	 * item is value that define the size of a item which is transported by a agent.
	 * When a station has no item's element, then the itemSize is zero.
	 */
	protected int item=0;
	/**
	 * This is number of agents which agents is located at this station.
	 * Per default, that is no agent.
	 */
	private int placesBusy=0;
	
	/**
	 * This is the exactly visited duration, which each agent has when it is visiting this stations.
	 * When a agent has its own duration, then visited duration into a given station is the min of both.  
	 * When a station has no item's element, then the itemSize is zero. That means no duration is defined for this station's type.
	 */
	protected int time=0;

	/**
	 * The name of a station. any station have to have a unique name.
	 */
	protected String name;
	
	/**
	 * HashTableofEdge gives the collection of nameEdge and the corresponding edge, which a agent can visit when that is possible.
	 * The nameEdge can be transformed via the method DefaultEdge.getTableName() to a table of two elements. The first is the incommingStation's name and the second is the outcommingStation's name. 
	 */

	protected Hashtable<String,DefaultEdge> hashTableofEdge;
	/**
	 * priority is a numeric value. When a agent can visit more stations, then station with high priority has to be prioritized.
	 * When this elements not exist, that the value is zero. 
	 */
	protected int priority=0;
	/**
	 * frequency is a numeric value and means that this stations has to be visited exactly n times by the agents 
	 * who allow to visit(see visitEdge's description) it, where n=frequency.
	 * When this elements not exist, that the value is zero. 
	 */
	protected int frequency=0;
	/**
	 * necessity is a numeric value and means that all agents that allow to visit(see visitEdge's description)
	 *  this station have to visit it exactly n times, where n=necessity.
	 *  When this elements not exist, that the value is zero. 
	 */
	protected int necessity=0;
	/**
	 * cycle is a numeric value and it is using for the timeEdge which allow to simulate how agents or stations working in parallel. 
	 * cycle has a sense when there are a timeEdge connected to this station. 
	 * When the timeEdge is directed, then outgoing cycle's value is the same as the cycle's value of incoming.
	 * When this elements not exist, that the value is zero or the value of incoming if exists. 
	 */
	protected int cycle=0;

	/**
	 * This constructor is use for make a copy of a object. 
	 * @param SwarmStationGeneric 
	 */
	
	public SwarmStationGeneric(SwarmStationGeneric swarmStationGeneric){
		super();
		this.stationId =swarmStationGeneric.stationId;
		this.name= swarmStationGeneric.name;
		this.count =swarmStationGeneric.count;
		this.space=swarmStationGeneric.space;
		this.item=swarmStationGeneric.item;
		this.time=swarmStationGeneric.time;
		this.priority=swarmStationGeneric.priority;
		this.frequency=swarmStationGeneric.frequency;
		this.necessity=swarmStationGeneric.necessity;
		this.cycle=swarmStationGeneric.cycle;
		this.hashTableofEdge= swarmStationGeneric.hashTableofEdge;
	}

	/**
	 * @param id is the station's id and has to be greater than zero.
	 * @param name has to be unique and no Null.
	 */
	
	public SwarmStationGeneric(String name, int stationId){
		super();
		this.name= name;
		this.stationId =stationId;
		hashTableofEdge=new Hashtable<String,DefaultEdge>();
	}

	/**
	 * @param space has to be greater than zero, otherwise an error would be occur.
	 * @param name has to be unique and no Null.
	 * @param stationId is the station's id and has to be greater than zero.
	 * @param count number of station, which have to be instanced. 
	 * @param item the size of item, which will be transported by a agent.
	 * @param time the work's duration 
	 * @param priority which station has to be prioritized.
	 * @param frequency what many time a station has exactly to be visited by all stations
	 * @param necessity what many time a station has exactly to be visited by one agent
	 * @param cycle a numeric value and it is using for the timeEdge which allow to simulate how agents or stations working in parallel.
	 */
	
	public SwarmStationGeneric(String name, int stationId ,int count, 	int space,	int item, int time,	int priority,	int frequency,	int necessity,	int cycle){
		super();
		this.stationId =stationId;
		this.name= name;
		this.count =count;
		this.space=space;
		this.item=item;
		this.time=time;
		this.priority=priority;
		this.frequency=frequency;
		this.necessity=necessity;
		this.cycle=cycle;
		hashTableofEdge=new Hashtable<String,DefaultEdge>();
	}

//	public int getStationId() {
//		return stationId;
//	}

	public String getStationName() {
		return name;
	}


	public int getPlacesBusy() {
		return placesBusy;
	}
	
	/**
	 * This increments of 1 the number of busy's Places
	 */
	public void incPlacesBusy() {
		if(isSpace()){
			this.placesBusy +=1;
		}
	}
	/**
	 * This decrements of 1 the number of busy's Places
	 */
	public void decPlacesBusy() {
		if(0<placesBusy){
			this.placesBusy -=1;
		}
	}
	/**
	 * 
	 * @return true when busied places are less than space.
	 */
	public boolean isSpace(){
		return placesBusy<space;
	}

	public int getSpace() {
		return space;
	}

	public int getItem() {
		return item;
	}



	public int getPriority() {
		return priority;
	}



	public int getFrequency() {
		return frequency;
	}



	public int getNecessity() {
		return necessity;
	}



	public int getCycle() {
		return cycle;
	}



	public int getCount() {
		return count;
	}



	public Hashtable<String, DefaultEdge> getHashTableofEdge() {
		return hashTableofEdge;
	}

	public int getTime() {
		return time;
	}

	public int getStationId() {
		return stationId;
	}	
	
}
