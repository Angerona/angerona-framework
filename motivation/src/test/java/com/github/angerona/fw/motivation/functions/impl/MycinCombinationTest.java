package com.github.angerona.fw.motivation.functions.impl;

import org.junit.Assert;
import org.junit.Test;

import com.github.angerona.fw.motivation.functional.impl.MycinCombination;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class MycinCombinationTest {

	private static final double X_POS = 0.86;
	private static final double Y_POS = 0.42;
	private static final double X_NEG = -0.27;
	private static final double Y_NEG = -0.93;

	private static final double POS_POS = 0.9188;
	private static final double NEG_NEG = -0.9489;
	private static final double POS_NEG = -0.5;
	private static final double NEG_POS = 0.15 / 0.73;

	@Test
	public void testCreateMycin() {
		@SuppressWarnings("unused")
		MycinCombination mycin = new MycinCombination();
	}

	@Test
	public void testAggregatePosPos() {
		MycinCombination mycin = new MycinCombination();
		Assert.assertEquals(POS_POS, mycin.aggregate(X_POS, Y_POS), 0.000001);
	}

	@Test
	public void testAggregateNegNeg() {
		MycinCombination mycin = new MycinCombination();
		Assert.assertEquals(NEG_NEG, mycin.aggregate(X_NEG, Y_NEG), 0.000001);
	}

	@Test
	public void testAggregatePosNeg() {
		MycinCombination mycin = new MycinCombination();
		Assert.assertEquals(POS_NEG, mycin.aggregate(X_POS, Y_NEG), 0.000001);
	}

	@Test
	public void testAggregateNegPos() {
		MycinCombination mycin = new MycinCombination();
		Assert.assertEquals(NEG_POS, mycin.aggregate(X_NEG, Y_POS), 0.000001);
	}

}
