package com.github.angerona.fw.motivation.model;

import static com.github.angerona.fw.motivation.dummies.DummyRanges.WR_PN;
import static com.github.angerona.fw.motivation.dummies.DummyRanges.WR_SA;

import org.junit.Assert;
import org.junit.Test;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class WeightRangeTest {

	private static final double DELTA_WR_PN = 0.6;
	private static final double DELTA_WR_SA = 0.6;

	private static final double MEAN_WR_PN = 0.7;
	private static final double MEAN_WR_SA = 0.3;

	@Test
	public void testGetDelta() {
		Assert.assertEquals(DELTA_WR_PN, WR_PN.getDelta(), 0.0);
		Assert.assertEquals(DELTA_WR_SA, WR_SA.getDelta(), 0.0);
	}

	@Test
	public void testGetMean() {
		Assert.assertEquals(MEAN_WR_PN, WR_PN.getMean(), 0.0);
		Assert.assertEquals(MEAN_WR_SA, WR_SA.getMean(), 0.0);
	}

}
