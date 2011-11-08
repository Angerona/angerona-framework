package net.sf.tweety;

import java.util.*;

/**
 * An abstract interpretation for some logical language.
 * @author Matthias Thimm
 */
public abstract class Interpretation {
	
	/**
	 * Checks whether this interpretation satisfies the given formula.
	 * @param formula a formula .
	 * @return "true" if this interpretation satisfies the given formula.
	 * @throws IllegalArgumentException if the formula does not correspond
	 * 		to the expected language.
	 */
	public abstract boolean satisfies(Formula formula) throws IllegalArgumentException;
	
	/**
	 * Checks whether this interpretation satisfies all given formulas.
	 * @param formulas a collection of formulas.
	 * @return "true" if this interpretation satisfies all given formulas. 
	 * @throws IllegalArgumentException if at least one formula does not correspond
	 * 		to the expected language.
	 */
	public boolean satisfies(Collection<? extends Formula> formulas) throws IllegalArgumentException{
		for(Formula f: formulas)
			if(!this.satisfies(f))
				return false;
		return true;
	}
	
	/**
	 * Checks whether this interpretation satisfies the given knowledge base.
	 * @param beliefBase a knowledge base.
	 * @return "true" if this interpretation satisfies the given knowledge base.
	 * @throws IllegalArgumentException IllegalArgumentException if the knowledgebase does not correspond
	 * 		to the expected language.
	 */
	public abstract boolean satisfies(BeliefBase beliefBase) throws IllegalArgumentException;
}
