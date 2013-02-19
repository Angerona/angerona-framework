package net.sf.tweety.logics.commons;

import net.sf.tweety.Formula;
import net.sf.tweety.math.probability.Probability;


/**
 * This interface models a classical formula, i.e. a formula that can be connected
 * to other classical formulas using AND and OR and where the complement is
 * well-defined.
 * @author Matthias Thimm
 *
 */
public interface ClassicalFormula extends Formula{
	
	/**
	 * Returns a conjunction of this and the given formula.
	 * @param f a formula to be combined with AND and this.
	 * @return a conjunction of this and the given formula.
	 */
	public ClassicalFormula combineWithAnd(ClassicalFormula f);
	
	/**
	 * Returns a disjunction of this and the given formula.
	 * @param f a formula to be combined with OR and this.
	 * @return a disjunction of this and the given formula.
	 */
	public ClassicalFormula combineWithOr(ClassicalFormula f);
	
	/**
	 * Returns the complement of this formula in a classic
	 * logical sense.
	 * @return the complement of this formula.
	 */
	public ClassicalFormula complement();
	
	/**
	 * Returns this formula's probability in the uniform distribution. 
	 * @return this formula's probability in the uniform distribution.
	 */
	public Probability getUniformProbability();
}
