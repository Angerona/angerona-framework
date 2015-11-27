package com.github.kreaturesfw.core.basic;

/**
 * This is the most basic definition of an agent, receiving perceptions and
 * performing actions accordingly
 * 
 * @author Manuel Barbi
 *
 */
public abstract class AbstractAgent<P extends Perception> {

	/**
	 * 
	 * @param perception
	 * @return whether an action was performed
	 */
	public abstract boolean execute(P perception);

}
