package com.whiplash.util;

/**
 * This class provides unique ID handling functionality.
 * 
 * @author Matthias Thimm
 */
public abstract class IdTools {

	/** The current id. */
	private static int id = 1;
	
	/** Returns an application wide unique id.
	 * @return an application wide unique id.
	 */
	public static int getNextId(){
		return IdTools.id++;
	}
	
	/** Returns an application wide unique id.
	 * @return an application wide unique id.
	 */
	public static String getNextIdAsString(){
		return new Integer(IdTools.id++).toString();
	}
}
