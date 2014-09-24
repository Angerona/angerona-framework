package com.github.angerona.fw.motivation.dao;

import com.github.angerona.fw.Desire;
import com.github.angerona.fw.motivation.plan.ActionSequence;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public interface ActionComponentDao<T extends Comparable<T>> {

	/**
	 * returns the ActionNode linked with Desire d
	 * 
	 * @param d
	 * @return
	 */
	public ActionSequence<T> get(Desire d);
	
	/**
	 * returns the ActionNode linked with Desire represented by key
	 * 
	 * @param d
	 * @return
	 */
	public ActionSequence<T> get(String key);

	/**
	 * links Desire d with an ActionNode
	 * 
	 * @param d
	 * @param actionId
	 */
	public void put(Desire d, ActionSequence<T> node);

}
