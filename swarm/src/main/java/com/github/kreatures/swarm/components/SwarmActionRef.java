package com.github.kreatures.swarm.components;

/**
 * This enum give the environnement information about what the agent want to do next.
 * @author donfack
 *
 */
public enum SwarmActionRef {
	/**
	 * A agent must wait that the station can be visited.
	 */
	WAIT,
	/**
	 * A agent enters a station in order to visit it.
	 */
	ENTER_STATION,
	/**
	 * A agent leaves a station and is on the way to visit a other station.
	 */
	LEAVE_STATION, 
	
	/**
	 * A agent is on the way.
	 */
	ON_WAY,
	/**
	 * 
	 */
	/**
	 * A agent is visiting a station and do its job.
	 */
	DO_WORK; //   
}