package com.github.angerona.fw.motivation.functional.impl;

import com.github.angerona.fw.motivation.GenLevelIterable;
import com.github.angerona.fw.motivation.Maslow;
import com.github.angerona.fw.motivation.MaslowIterable;
import com.github.angerona.fw.motivation.functional.DeficiencyPartition;
import com.github.angerona.fw.motivation.functional.WeightAdjustment;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class WeightAdjustmentImpl extends GenWeightAdjustmentImpl<Maslow> {

	public WeightAdjustmentImpl() {
		super(new MaslowDeficiency(), new MaslowIterable());
	}

	public WeightAdjustmentImpl(DeficiencyPartition<Maslow> partition, GenLevelIterable<Maslow> levels) {
		super(partition, levels);
	}

	@Override
	public WeightAdjustment<Maslow> copy() {
		return new WeightAdjustmentImpl(this.partition.copy(), this.levels.copy());
	}

}
