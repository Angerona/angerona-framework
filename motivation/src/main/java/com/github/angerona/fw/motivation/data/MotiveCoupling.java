package com.github.angerona.fw.motivation.data;

import net.sf.tweety.logics.fol.syntax.FolFormula;

import com.github.angerona.fw.Desire;

/**
 * 
 * @author Manuel Barbi
 * 
 * @param <L>
 */
public class MotiveCoupling<L extends MotiveLevel> {

	private Motive<L> motive;
	private Desire desire;
	private double couplingStrength;
	private FolFormula statement;

	public MotiveCoupling(Motive<L> motive, Desire desire, double couplingStrength, FolFormula statement) {
		if (motive == null) {
			throw new NullPointerException("motive must not be null");
		}

		if (desire == null) {
			throw new NullPointerException("desire must not be null");
		}

		if (couplingStrength < -1 || couplingStrength > 1) {
			throw new IllegalArgumentException("coupling-strength must lie between (-1) and 1");
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

	public FolFormula getStatement() {
		return statement;
	}

	@Override
	public String toString() {
		return "(" + motive + "; " + desire + "; " + couplingStrength + "; " + (statement != null ? statement : "true") + ")";
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
		@SuppressWarnings("unchecked")
		MotiveCoupling<L> other = (MotiveCoupling<L>) obj;
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
