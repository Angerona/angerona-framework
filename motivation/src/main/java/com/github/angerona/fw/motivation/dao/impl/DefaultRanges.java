package com.github.angerona.fw.motivation.dao.impl;

import static com.github.angerona.fw.motivation.Maslow.ESTEEM;
import static com.github.angerona.fw.motivation.Maslow.LOVE_AND_BELONGING;
import static com.github.angerona.fw.motivation.Maslow.PHYSIOLOGICAL_NEEDS;
import static com.github.angerona.fw.motivation.Maslow.SAFETY_NEEDS;
import static com.github.angerona.fw.motivation.Maslow.SELF_ACTUALIZATION;

import com.github.angerona.fw.motivation.model.WeightRange;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class DefaultRanges extends WeightRanges {

	public DefaultRanges() {
		this.ranges.put(PHYSIOLOGICAL_NEEDS, new WeightRange(0.75, 1.00));
		this.ranges.put(SAFETY_NEEDS, new WeightRange(0.55, 0.85));
		this.ranges.put(LOVE_AND_BELONGING, new WeightRange(0.35, 0.65));
		this.ranges.put(ESTEEM, new WeightRange(0.15, 0.45));
		this.ranges.put(SELF_ACTUALIZATION, new WeightRange(0.00, 0.25));
	}

}
