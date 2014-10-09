package com.github.angerona.fw.motivation.dao;

import com.github.angerona.fw.Agent;
import com.github.angerona.fw.motivation.MotiveLevel;

/**
 * {@link LevelWeightDao} is a generic class, that delivers access to the set of LevelWeights of an {@link Agent}.
 * 
 * @author Manuel Barbi
 * 
 * @param <L>
 */
public interface LevelWeightDao<L extends MotiveLevel> {

	public double getWeight(L level);

	public void putWeight(L level, double w);

}
