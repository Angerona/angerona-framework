package com.github.angerona.fw.motivation.functional.impl;

import com.github.angerona.fw.motivation.functional.AggregationFunction;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class MycinCombination implements AggregationFunction {

	@Override
	public double aggregate(double x, double y) {
		if (x > 0 && y > 0) {
			return (x + y) - (x * y);
		} else if (x < 0 && y < 0) {
			return (x + y) + (x * y);
		} else {
			return (x + y) / (1 - Math.min(Math.abs(x), Math.abs(y)));
		}
	}

	@Override
	public AggregationFunction copy() {
		return new MycinCombination();
	}

}
