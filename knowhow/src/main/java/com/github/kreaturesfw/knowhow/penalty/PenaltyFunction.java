package com.github.kreaturesfw.knowhow.penalty;

import com.github.kreaturesfw.core.basic.Action;
import com.github.kreaturesfw.core.basic.Agent;

import net.sf.tweety.logics.fol.syntax.FolFormula;

/**
 * 
 * 
 * @author Tim Janus
 */
public interface PenaltyFunction extends Cloneable {
	
	/**
	 * Initializes the penalty function for the given agent, after this call
	 * iterations returns zero.
	 * @param agent	The agent thats' actions are tested with the penalty function
	 */
	void init(Agent agent);

	/** @return The count of iterations the penalty function is used for */
	int iterations();
	
	/**
	 * Processes the penalty for the given action, if i is the return value of iterations() before
	 * the call then i+1 is it's return value after the call.
	 * 
	 * @param action	The action that's penalty value is checked
	 * @return			A double representing the penalty value of the action
	 * @throws IllegalStateException 	Is thrown if the init() method was not called before
	 * 									this method. There might be implementations of this
	 * 									interface that does not need init to be called so also
	 * 									read the documentation of the implementing class
	 */
	double penalty(Action action) throws IllegalStateException;
	
	boolean validCondition(FolFormula formula);
	
	PenaltyFunction clone();
}
