package com.github.kreatures.swarm.components;

import java.util.Hashtable;
import java.util.List;

/**
 * 
 * @author Donfack
 * SwarmMapping is a set of methode which allow to map a class with its name as key.
 * this garantee that two class' objects never have the same name.    
 */

public interface SwarmMapping {
	
	/**
	 * 
	 * @param stationName The name of a station. This has to be unique.
	 * @return the instance of station whose name is stationName  
	 */
	public DefaultStation getStationIntance (String stationName);
	/**
	 * 
	 * @param departStationName The name of a first station . This has to be unique.
	 * @param arriveStationName The name of a second station. This has to be unique.
	 * @return DefaultEdge which connects firstStationName with secondStationName  
	 */
	
	
	public DefaultEdge getEdgeIntance(String departStationName, String arriveStationName);
	
//	/**
//	 * 
//	 * @param stationId The id of a station. This has to be unique.
//	 * @return the instance of station whose Id is stationId  
//	 */
//	public DefaultStation getStationIntance (int stationId);
	
//	/**
//	 * 
//	 * @param edgeId The Id of a edge. This has to be unique.
//	 * @return the instance of edge whose name is edgeName
//	 */
//	
//	public DefaultEdge getEdgeIntance(int edgeId);
	
	/**
	 * 
	 * @param stationName The name of a station. This has to be unique.
	 * @return all edges whose incommingstation's name is stationName. Incommingstation can also be outcommingsation if the edge is undirected.
	 */
	public Hashtable<String,DefaultEdge> getAllEdgeOfSation(String stationName);
}
