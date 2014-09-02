package com.github.angerona.fw.motivation.dummies;

import static com.github.angerona.fw.motivation.Maslow.ESTEEM;
import static com.github.angerona.fw.motivation.Maslow.LOVE_AND_BELONGING;
import static com.github.angerona.fw.motivation.Maslow.PHYSIOLOGICAL_NEEDS;
import static com.github.angerona.fw.motivation.Maslow.SAFETY_NEEDS;
import static com.github.angerona.fw.motivation.Maslow.SELF_ACTUALIZATION;

import com.github.angerona.fw.motivation.dao.impl.WeightRanges;
import com.github.angerona.fw.motivation.model.WeightRange;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class DummyRanges extends WeightRanges {

	public static final WeightRange WR_PN = new WeightRange(0.4, 1);
	public static final WeightRange WR_SA = new WeightRange(0, 0.6);
	public static final WeightRange WR_UN = new WeightRange(0, 1);

	public DummyRanges() {
		this.ranges.put(PHYSIOLOGICAL_NEEDS, WR_PN);
		this.ranges.put(SAFETY_NEEDS, WR_UN);
		this.ranges.put(LOVE_AND_BELONGING, WR_UN);
		this.ranges.put(ESTEEM, WR_UN);
		this.ranges.put(SELF_ACTUALIZATION, WR_SA);
	}

}
