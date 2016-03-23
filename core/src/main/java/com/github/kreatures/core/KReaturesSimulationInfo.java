package com.github.kreatures.core;
/**
 * This class takes the initial importance informations about the current simulation. 
 * This only happens when the simulation is initializing. 
 * @author donfack
 *
 */

public class KReaturesSimulationInfo {
	/** 
	 * the name of the simulation 
	 */
	private static String name=null;
	/** 
	 * the root folder of the actual loaded simulation in this environment 
	 */
	private static String simDirectory=null;
	
	/**
	 * 
	 * @return the name of the simulation
	 */
	public static String getName() {
		return name;
	}
	/**
	 * 
	 * @param name the name of the simulation
	 */
	protected static void setName(String name) {
		KReaturesSimulationInfo.name = name;
	}
	/** 
	 * @return the root folder of the actual loaded simulation in this environment
	 */
	public static String getSimDirectory() {
		return simDirectory;
	}
	/**  
	 * @param simDirectory the root folder of the actual loaded simulation in this environment
	 */
	protected static void setSimDirectory(String simDirectory) {
		KReaturesSimulationInfo.simDirectory = simDirectory;
	}
	
	

}
