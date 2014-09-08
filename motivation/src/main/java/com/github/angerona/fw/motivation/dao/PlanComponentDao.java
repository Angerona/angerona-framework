package com.github.angerona.fw.motivation.dao;

import java.util.Collection;

import com.github.angerona.fw.Desire;
import com.github.angerona.fw.motivation.plan.StateNode;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public interface PlanComponentDao {

	public Collection<StateNode> getPlan(Desire d);

	public Collection<StateNode> getPlan(String key);

}
