package com.github.angerona.fw.motivation.model;

import net.sf.tweety.Formula;

import com.github.angerona.fw.Desire;
import com.github.angerona.fw.motivation.MotiveLevel;

/**
 * 
 * @author Manuel Barbi
 * 
 * @param <L>
 * @param <F>
 */
public class MotiveCoupling<L extends MotiveLevel, F extends Formula> {

	private Motive<L> motive;
	private Desire desire;
	private double couplingStrength;
	private F statement;

	public MotiveCoupling(Motive<L> motive, Desire desire, double couplingStrength, F statement) {
		if (motive == null) {
			throw new NullPointerException("motive must not be null");
		}

		if (desire == null) {
			throw new NullPointerException("desire must not be null");
		}

		if (couplingStrength < -1 || couplingStrength > 1) {
			throw new IllegalArgumentException("coupling-strength must lie between (-1) and 1");
		}

		if (statement == null) {
			throw new NullPointerException("statement must not be null");
		}

		this.motive = motive;
		this.desire = desire;
		this.couplingStrength = couplingStrength;
		this.statement = statement;
	}

	public Motive<L> getMotive() {
		return motive;
	}

	public Desire getDesire() {
		return desire;
	}

	public double getCouplingStrength() {
		return couplingStrength;
	}

	public F getStatement() {
		return statement;
	}

	@Override
	public String toString() {
		return "(" + motive + "; " + desire + "; " + couplingStrength + "; " + statement + ")";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(couplingStrength);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((desire == null) ? 0 : desire.hashCode());
		result = prime * result + ((motive == null) ? 0 : motive.hashCode());
		result = prime * result + ((statement == null) ? 0 : statement.hashCode());
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
		MotiveCoupling<?, ?> other = (MotiveCoupling<?, ?>) obj;
		if (Double.doubleToLongBits(couplingStrength) != Double.doubleToLongBits(other.couplingStrength))
			return false;
		if (desire == null) {
			if (other.desire != null)
				return false;
		} else if (!desire.equals(other.desire))
			return false;
		if (motive == null) {
			if (other.motive != null)
				return false;
		} else if (!motive.equals(other.motive))
			return false;
		if (statement == null) {
			if (other.statement != null)
				return false;
		} else if (!statement.equals(other.statement))
			return false;
		return true;
	}

}
