package com.github.angerona.fw.motivation.dao;

import com.github.angerona.fw.motivation.MotiveLevel;
import com.github.angerona.fw.motivation.model.WeightRange;

/**
 * 
 * @author Manuel Barbi
 * 
 * @param <L>
 */
public interface WeightRangeDao<L extends MotiveLevel> {

	public WeightRange getRange(L level);

}
