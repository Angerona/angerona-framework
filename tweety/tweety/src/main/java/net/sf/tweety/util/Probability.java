package net.sf.tweety.util;

/**
 * This class represents a probability, i.e. a double in the interval [0,1].
 * 
 * @author Matthias Thimm
 */
public class Probability extends Number {

	/**
	 * The precision for probabilities.
	 * TODO: that should go somewhere else. 
	 */
	public final static double PRECISION = 0.01;
	
	/**
	 * For serialization
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The actual value of the probability.
	 */
	private Double value;
	
	/**
	 * Creates a new probability with the given value
	 * @param value a double in the interval [0,1]
	 */
	public Probability(Double value){
		if(value < 0 && value > - Probability.PRECISION )
			value = 0d;
		if(value > 1 && value < 1 + Probability.PRECISION)
			value = 1d;
		if(value < 0 || value > 1)
			throw new IllegalArgumentException("Probabilities must have a value in [0,1].");
		this.value = value;
	}
	
	/**
	 * Returns the complement of this probability, i.e. a probability of
	 * one minus the value of this probability.
	 * @return the complement of this probability.
	 */
	public Probability complement(){
		return new Probability(1-this.value);
	}
	
	/**
	 * Computes the sum of this and the given probability.
	 * @param other a probability.
	 * @return a probability.
	 */
	public Probability add(Probability other){
		return new Probability(this.value + other.getValue());
	}
	
	/**
	 * Computes the product of this probability and the given number.
	 * @param other a number.
	 * @return a probability.
	 */
	public Probability mult(Double other){
		return new Probability(this.value * other);
	}
	
	/**
	 * Computes the product of this probability and the given number.
	 * @param other a number.
	 * @return a probability.
	 */
	public Probability mult(Integer other){
		return new Probability(this.value * other);
	}
	
	/**
	 * Divides this probability by other and returns the result.
	 * @param other a probability.
	 * @return a probability.
	 */
	public Probability divide(Probability other){
		return new Probability(this.value/other.getValue());
	}
	
	/**
	 * Divides this probability by given value returns the result.
	 * @param other a double value.
	 * @return a probability.
	 */
	public Probability divide(Double other){
		return new Probability(this.value/other);
	}
	
	/** Checks whether the given probability is "nearly" the same
	 * as this probability (given the actual precision).
	 * @param other some probability
	 * @return "true" if the given probability is "nearly" the
	 * same as this one.
	 */
	public boolean isWithinTolerance(Probability other){
		return this.value >= other.value - Probability.PRECISION &&
			this.value <= other.value + Probability.PRECISION;
	}
	
	/**
	 * Returns the value of this probability.
	 * @return the value of this probability.
	 */
	public Double getValue(){
		return this.value;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Number#doubleValue()
	 */
	@Override
	public double doubleValue() {
		return this.value.doubleValue();
	}

	/* (non-Javadoc)
	 * @see java.lang.Number#floatValue()
	 */
	@Override
	public float floatValue() {
		return this.value.floatValue();
	}

	/* (non-Javadoc)
	 * @see java.lang.Number#intValue()
	 */
	@Override
	public int intValue() {
		return this.intValue();
	}

	/* (non-Javadoc)
	 * @see java.lang.Number#longValue()
	 */
	@Override
	public long longValue() {
		return this.value.longValue();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		return this.value.toString();
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Probability other = (Probability) obj;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}
	
}
