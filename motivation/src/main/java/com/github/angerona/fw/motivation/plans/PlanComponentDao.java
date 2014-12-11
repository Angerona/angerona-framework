package com.github.angerona.fw.motivation.plans;

import java.util.Collection;

import com.github.angerona.fw.Desire;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public interface PlanComponentDao {

	public Collection<StateNode> getPlan(Desire d);

	public Collection<StateNode> getPlan(String key);

}
