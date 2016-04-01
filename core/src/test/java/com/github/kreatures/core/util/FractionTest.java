package com.github.kreatures.core.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class FractionTest {
	
	@Test
	public void testFractionGenerationOneHalf() {
		Pair<Integer, Integer> aHalf = Utility.decimalToFraction(0.5);
		assertEquals(1, aHalf.first.intValue());
		assertEquals(2, aHalf.second.intValue());
	}
	
	@Test
	public void testFractionGenerationOneThird() {
		Pair<Integer, Integer> aThird = Utility.decimalToFraction(0.33333);
		assertEquals(1, aThird.first.intValue());
		assertEquals(3, aThird.second.intValue());
	}
	
	@Test
	public void testFractionGenerationTwoThird() {
		Pair<Integer, Integer> aThird = Utility.decimalToFraction(0.66666);
		assertEquals(2, aThird.first.intValue());
		assertEquals(3, aThird.second.intValue());
	}
	
	@Test
	public void testFractionGenerationZero() {
		Pair<Integer, Integer> aThird = Utility.decimalToFraction(0);
		assertEquals(0, aThird.first.intValue());
		assertEquals(1, aThird.second.intValue());
	}
	
	@Test
	public void testFractionGenerationOne() {
		Pair<Integer, Integer> aThird = Utility.decimalToFraction(1);
		assertEquals(1, aThird.first.intValue());
		assertEquals(1, aThird.second.intValue());
	}
	
	@Test
	public void testFractionGenerationTwoAndAFith() {
		Pair<Integer, Integer> aThird = Utility.decimalToFraction(2.2);
		assertEquals(11, aThird.first.intValue());
		assertEquals(5, aThird.second.intValue());
	}
}
