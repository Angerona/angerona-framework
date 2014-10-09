package com.github.angerona.fw.motivation.functional;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public interface AggregationFunction {

	public double aggregate(double x, double y);

	public AggregationFunction copy();

}
