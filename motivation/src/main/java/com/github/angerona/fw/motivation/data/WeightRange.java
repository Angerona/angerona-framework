package com.github.angerona.fw.motivation.data;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class WeightRange {

	private double lower = 0;
	private double upper = 1;

	public WeightRange(double lower, double upper) {
		if (lower < 0 || lower > 1) {
			throw new IllegalArgumentException("lower bound must lie between 0 and 1");
		}

		if (upper < 0 || upper > 1) {
			throw new IllegalArgumentException("upper bound must lie between 0 and 1");
		}

		if (lower > upper) {
			throw new IllegalArgumentException("lower bound must not be greater then upper");
		}

		this.lower = lower;
		this.upper = upper;
	}

	public double getLower() {
		return lower;
	}

	public double getUpper() {
		return upper;
	}

	public double getDelta() {
		return upper - lower;
	}

	public double getMean() {
		return (upper + lower) / 2;
	}

	@Override
	public String toString() {
		return "[" + lower + ", " + upper + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(lower);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(upper);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WeightRange other = (WeightRange) obj;
		if (Double.doubleToLongBits(lower) != Double.doubleToLongBits(other.lower))
			return false;
		if (Double.doubleToLongBits(upper) != Double.doubleToLongBits(other.upper))
			return false;
		return true;
	}

}
