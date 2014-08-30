package com.github.angerona.fw.motivation.dao;

import com.github.angerona.fw.motivation.MotiveLevel;

/**
 * 
 * @author Manuel Barbi
 * 
 * @param <L>
 */
public interface LevelWeightDao<L extends MotiveLevel> {

	public double getWeight(L level);

	public void putWeight(L level, double w);

}
