package com.github.angerona.fw.motivation.dao;

import java.util.Collection;

import net.sf.tweety.Formula;

import com.github.angerona.fw.Desire;
import com.github.angerona.fw.motivation.plan.StateNode;

/**
 * 
 * @author Manuel Barbi
 * 
 * @param <F>
 */
public interface PlanComponentDao<F extends Formula> {

	public Collection<StateNode<F>> getPlan(Desire d);

}
