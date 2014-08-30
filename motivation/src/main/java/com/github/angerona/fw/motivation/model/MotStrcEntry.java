package com.github.angerona.fw.motivation.model;

import com.github.angerona.fw.Desire;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class MotStrcEntry implements Comparable<MotStrcEntry> {

	private Desire desire;
	private double motivationalValue;

	public MotStrcEntry(Desire desire, double motivationalValue) {
		if (desire == null) {
			throw new NullPointerException("desire must not be null");
		}

		if (motivationalValue < -1 || motivationalValue > 1) {
			throw new IllegalArgumentException("motivational-value must lie between (-1) and 1");
		}

		this.desire = desire;
		this.motivationalValue = motivationalValue;
	}

	public Desire getDesire() {
		return desire;
	}

	public double getMotivationalValue() {
		return motivationalValue;
	}

	@Override
	public String toString() {
		return "MotStrcEntry [" + desire + ", " + motivationalValue + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((desire == null) ? 0 : desire.hashCode());
		long temp;
		temp = Double.doubleToLongBits(motivationalValue);
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
		MotStrcEntry other = (MotStrcEntry) obj;
		if (desire == null) {
			if (other.desire != null)
				return false;
		} else if (!desire.equals(other.desire))
			return false;
		if (Double.doubleToLongBits(motivationalValue) != Double.doubleToLongBits(other.motivationalValue))
			return false;
		return true;
	}

	@Override
	public int compareTo(MotStrcEntry o) {
		return Double.compare(this.motivationalValue, o.motivationalValue);
	}

}
