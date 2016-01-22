package com.github.kreatures.swarm.components;

import java.util.LinkedList;
import java.util.List;

import com.github.kreatures.core.BaseAgentComponent;

/**
 * 
 * @author Donfack
 *This Class SwarmStationGeneric is a station's generic. All station's types have to extends this class. 
 */
public abstract class SwarmStationGeneric extends BaseAgentComponent implements SwarmStation{
	private int stationId;
	/**
	 * This is number of places where agents can work at the same time into the station.
	 * Per default, that is one place.
	 */
	protected int stationCapacity=1;

	/**
	 * This is number of agents which agents is located at this station.
	 * Per default, that is no agent.
	 */
	private int placesBusy=0;

	/**
	 * The name of a station. any station have to have a unique name.
	 */
	protected String name;
	/**
	 * The listofEdge gives the list of Edge, which a agent can visit when that is possible. 
	 */
	//protected LinkedList<Class<?  extends SwarmEdge>> listofEdge;
	protected List<DefaultEdge> listofEdge;

	/**
	 * @param id is the station's id and has to be greater than zero.
	 * @param stationCapacity has to be greater than zero, otherwise an error would be occur.
	 * @param name has to be unique and no Null.
	 */
	public SwarmStationGeneric(int id, int stationCapacity, String name){
		this.stationCapacity=stationCapacity;
		this.stationId=id;
		this.name= name;
		listofEdge=new LinkedList<DefaultEdge>();
	}



	public int getStationId() {
		return stationId;
	}



	public int getStationCapacity() {
		return stationCapacity;
	}



	public String getName() {
		return name;
	}



	public List<DefaultEdge> getListofEdge() {
		return listofEdge;
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
		return placesBusy<stationCapacity;
	}

	public int getCapacity() {
		return stationCapacity;
	}

}
