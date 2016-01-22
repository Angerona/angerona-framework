package com.github.kreatures.swarm.serialize;

import java.util.List;

import com.github.kreatures.core.serialize.Resource;


/**
 * This is an Interface of serialized XML-file which will be created by a swarm-scenario.   
 * 
 * 
 * @author donfack
 *
 */

public interface SwarmConfig extends Resource {
	public static final String RESOURCE_TYPE = "logisticsGraph";

	
	/**
	 * 
	 * @return all the perspective within a scenario.
	 */
//	public List<SwarmPerception> getSwarmPerception(); 
	/**
	 * 
	 * @return all stations which are within one swam scenario.
	 */
//	public List<SwarmStation> getSwarmStation();
	
//	public List<visit>
}