package com.github.angerona.fw.motivation.dao;

import java.util.Collection;

import net.sf.tweety.Formula;

import com.github.angerona.fw.Desire;
import com.github.angerona.fw.motivation.plan.StateNode;

/**
 * 
 * @author Manuel Barbi
 * 
 * @param <ID>
 * @param <F>
 */
public interface PlanComponentDao<ID extends Comparable<ID>, F extends Formula> {

	public Collection<StateNode<ID, F>> getPlan(Desire d);

	public F getRelCondition(Desire d);

	public F getRelAlternatives(Desire d);

}
