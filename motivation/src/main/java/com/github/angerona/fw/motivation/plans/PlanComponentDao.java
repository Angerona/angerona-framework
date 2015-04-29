package com.github.angerona.fw.motivation.plans;

import com.github.angerona.fw.Desire;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public interface PlanComponentDao {

	public Trail[] getTrails(Desire d);

	public Trail[] getTrails(String key);

}
