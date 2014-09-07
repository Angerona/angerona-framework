package com.github.angerona.fw.motivation.dao;

import com.github.angerona.fw.Desire;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public interface ActionComponentDao<T extends Comparable<T>> {

	/**
	 * returns the action linked with Desire d or null otherwise
	 * 
	 * @param d
	 * @return
	 */
	public T get(Desire d);

	/**
	 * checks whether an action is linked to Desire d
	 * 
	 * @param d
	 * @return
	 */
	public boolean exists(Desire d);

	/**
	 * links Desire d with an action if actionId is null the entry is removed
	 * 
	 * @param d
	 * @param actionId
	 */
	public void put(Desire d, T actionId);

}
