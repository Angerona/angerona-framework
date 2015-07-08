package com.github.angerona.fw.motivation.functional.impl;

import com.github.angerona.fw.motivation.data.Maslow;
import com.github.angerona.fw.motivation.functional.AggregationFunction;
import com.github.angerona.fw.motivation.functional.MotivationAdjustment;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class MotivationAdjustmentImpl extends GenMotAdjustmentImpl<Maslow> {

	public MotivationAdjustmentImpl() {
		super(new MycinCombination());
	}

	public MotivationAdjustmentImpl(AggregationFunction aggregation) {
		super(aggregation);
	}

	@Override
	public MotivationAdjustment<Maslow> copy() {
		return new MotivationAdjustmentImpl(this.aggregation.copy());
	}

}
