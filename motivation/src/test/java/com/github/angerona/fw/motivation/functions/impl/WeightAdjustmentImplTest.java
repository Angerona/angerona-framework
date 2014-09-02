package com.github.angerona.fw.motivation.functions.impl;

import static com.github.angerona.fw.motivation.Maslow.PHYSIOLOGICAL_NEEDS;
import static com.github.angerona.fw.motivation.Maslow.SELF_ACTUALIZATION;

import org.junit.Assert;
import org.junit.Test;

import com.github.angerona.fw.motivation.dao.impl.LevelWeights;
import com.github.angerona.fw.motivation.dummies.DummyBeliefState;
import com.github.angerona.fw.motivation.dummies.DummyMotiveStateTwo;
import com.github.angerona.fw.motivation.functional.impl.WeightAdjustmentImpl;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class WeightAdjustmentImplTest {

	private static double W_PN = 0.4;
	private static double W_SA = 0.6;

	@Test
	public void testCreateAdjustment() {
		@SuppressWarnings("unused")
		WeightAdjustmentImpl adjustment = new WeightAdjustmentImpl();
	}

	@Test
	public void testCalcRelSit() {
		WeightAdjustmentImpl adjustment = new WeightAdjustmentImpl();
		Assert.assertEquals(1, adjustment.rel_sit(new DummyMotiveStateTwo(), new DummyBeliefState()), 0.0);
	}

	@Test
	public void testAdjust() {
		LevelWeights weights = new LevelWeights();
		WeightAdjustmentImpl adjustment = new WeightAdjustmentImpl();
		adjustment.adjust(new DummyMotiveStateTwo(), new DummyBeliefState(), weights);

		Assert.assertEquals(W_PN, weights.getWeight(PHYSIOLOGICAL_NEEDS), 0.0);
		Assert.assertEquals(W_SA, weights.getWeight(SELF_ACTUALIZATION), 0.0);

	}

}
