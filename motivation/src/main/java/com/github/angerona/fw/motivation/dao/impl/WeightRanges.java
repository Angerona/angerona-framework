package com.github.angerona.fw.motivation.dao.impl;

import java.util.Map;

import com.github.angerona.fw.motivation.Maslow;
import com.github.angerona.fw.motivation.model.WeightRange;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class WeightRanges extends GenWeightRanges<Maslow> {

	public WeightRanges() {}

	public WeightRanges(Map<Maslow, WeightRange> ranges) {
		super(ranges);
	}

	@Override
	protected GenWeightRanges<Maslow> create() {
		return new WeightRanges();
	}

}
