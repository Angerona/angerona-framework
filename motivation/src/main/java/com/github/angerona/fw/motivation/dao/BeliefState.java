package com.github.angerona.fw.motivation.dao;

import net.sf.tweety.Formula;

import com.github.angerona.fw.Desire;

/**
 * 
 * @author Manuel Barbi
 * 
 * @param <F>
 */
public interface BeliefState<F extends Formula> {

	public boolean isReliable(Desire d);

	public boolean isSatisfied(Desire d);

	public boolean verify(F statement);

}
