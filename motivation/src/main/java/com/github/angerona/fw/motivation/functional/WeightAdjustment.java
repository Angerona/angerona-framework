package com.github.angerona.fw.motivation.functional;

import com.github.angerona.fw.motivation.MotiveLevel;
import com.github.angerona.fw.motivation.dao.BeliefState;
import com.github.angerona.fw.motivation.dao.LevelWeightDao;
import com.github.angerona.fw.motivation.dao.MotiveState;

/**
 * 
 * @author Manuel Barbi
 * 
 * @param <L>
 */
public interface WeightAdjustment<L extends MotiveLevel> {

	public void adjust(MotiveState<L> ms, BeliefState b, LevelWeightDao<L> out);

	public WeightAdjustment<L> copy();

}
