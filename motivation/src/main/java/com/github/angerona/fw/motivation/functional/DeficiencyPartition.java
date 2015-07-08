package com.github.angerona.fw.motivation.functional;

import com.github.angerona.fw.motivation.data.MotiveLevel;

/**
 * 
 * @author Manuel Barbi
 * 
 * @param <L>
 */
public interface DeficiencyPartition<L extends MotiveLevel> {

	public boolean contains(L level);

	public DeficiencyPartition<L> copy();

}
