package com.github.angerona.fw.motivation.dao;

import net.sf.tweety.logics.fol.syntax.FolFormula;

import com.github.angerona.fw.Desire;

/**
 * 
 * 
 * @author Manuel Barbi
 * 
 */
public interface BeliefState {

	/**
	 * @param d
	 * @return whether d is reliable achievable with some sequence of actions
	 */
	public boolean isReliable(Desire d);

	/**
	 * @param d
	 * @return whether d is already satisfied in the current situation
	 */
	public boolean isSatisfied(Desire d);

	/**
	 * verify some {@link FolFormula} for example the statement of a {@link MotiveCouplings}
	 * 
	 * @param statement
	 * @return
	 */
	public boolean verify(FolFormula statement);

}
