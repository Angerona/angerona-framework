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
	 * Is_currentSimChange is true when current simulation changing or false otherwise.
	 */
	private static boolean is_currentSimChange=false;
	/**
	 * This method is use to check either the current simulation has been changed or not.
	 * @return true when current simulation changing or false otherwise.
	 */
	public static boolean is_currentSimChange() {
		return is_currentSimChange;
	}
	/**
	 * @param is_currentSimChange is true when current simulation changing or false otherwise.
	 */
	public static void setIscurrentSimChange(boolean is_CurrentSimChange) {
		is_currentSimChange = is_CurrentSimChange;
	}
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
