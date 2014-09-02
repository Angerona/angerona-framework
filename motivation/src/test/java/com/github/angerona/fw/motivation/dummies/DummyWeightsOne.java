package com.github.angerona.fw.motivation.dummies;

import static com.github.angerona.fw.motivation.Maslow.ESTEEM;
import static com.github.angerona.fw.motivation.Maslow.LOVE_AND_BELONGING;
import static com.github.angerona.fw.motivation.Maslow.PHYSIOLOGICAL_NEEDS;
import static com.github.angerona.fw.motivation.Maslow.SAFETY_NEEDS;
import static com.github.angerona.fw.motivation.Maslow.SELF_ACTUALIZATION;

import com.github.angerona.fw.motivation.dao.impl.LevelWeights;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class DummyWeightsOne extends LevelWeights {

	public DummyWeightsOne() {
		this.putWeight(PHYSIOLOGICAL_NEEDS, 0.75);
		this.putWeight(SAFETY_NEEDS, 0);
		this.putWeight(LOVE_AND_BELONGING, 0);
		this.putWeight(ESTEEM, 0.45);
		this.putWeight(SELF_ACTUALIZATION, 0.25);
	}

}
