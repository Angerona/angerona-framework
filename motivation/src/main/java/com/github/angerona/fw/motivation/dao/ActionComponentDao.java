package com.github.angerona.fw.motivation.dao;

import com.github.angerona.fw.Desire;
import com.github.angerona.fw.motivation.model.ActionNode;

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
	public ActionNode<T> get(Desire d);

	/**
	 * links Desire d with an ActionNode
	 * 
	 * @param d
	 * @param actionId
	 */
	public void put(Desire d, ActionNode<T> node);

}
