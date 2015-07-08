package com.github.angerona.fw.motivation.dao;

import com.github.angerona.fw.motivation.data.MotiveLevel;
import com.github.angerona.fw.motivation.data.WeightRange;

/**
 * 
 * @author Manuel Barbi
 * 
 * @param <L>
 */
public interface WeightRangeDao<L extends MotiveLevel> {

	public WeightRange getRange(L level);

}
