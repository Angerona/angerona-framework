package com.github.angerona.fw.util;

import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;

import com.github.angerona.fw.logic.AngeronaAnswer;
import com.github.angerona.fw.logic.AnswerValue;

/**
 * A class containing utility methods.
 * 
 * @author Tim Janus
 */
public class Utility {
	/**
	 * Checks if the both object o1 and o2 are equal.
	 * @param o1	The first object
	 * @param o2	The second object
	 * @return		true if o1 equals o2, false otherwise.
	 */
	public static boolean equals(Object o1, Object o2) {
		if(o1 == null)	{
			return o2 == null;
		}
		return o1.equals(o2);
	}
	
	/**
	 * Computes the fraction that represent the given decimal value.
	 * @param x A decimal number represented as double
	 * @return	A Integer pair that represents the given decimal as a fraction, 
	 * 			whereby the first element represents the numerator and the 
	 * 			second element represents the denominator.
	 */
	public static Pair<Integer, Integer> decimalToFraction(double x) {
		final double error = 0.00001f;
		int n = (int)Math.floor(x);
		x -= n;
		if (x < error) {
			return new Pair<>(n, 1);
		} else if( 1-error < x) {
			return new Pair<>(n+1, 1);
		}
		
		// The lower fraction is 0/1
		int lower_n = 0;
		int lower_d = 1;
		// The upper fraction is 1/1
		int upper_n = 1;
		int upper_d = 1;
		
		while(true) {
	        // The middle fraction is (lower_n + upper_n) / (lower_d + upper_d)
		    int middle_n = lower_n + upper_n;
		    int middle_d = lower_d + upper_d;
		    // If x + error < middle
		    if (middle_d * (x + error) < middle_n) {
		    	// middle is our new upper
		    	upper_n = middle_n;
		    	upper_d = middle_d;
		    } else if(middle_n < (x - error) * middle_d) {
		    	// middle is our new lower
		    	lower_n = middle_n;
		    	lower_d = middle_d;
		    } else {
		    	return new Pair<>(n * middle_d + middle_n, middle_d);
		    }
		}
	}
	
	/**
	 * Computes the decimal representation of a given fraction
	 * @param fraction	An integer pair that represents the fraction, whereby
	 * 					the first element represents the numerator and the 
	 * 					second element represents the denominator
	 * @return			A double that represents the given fraction as decimal number
	 */
	public static double fractionToDecimal(Pair<Integer, Integer> fraction) {
		return fraction.first.intValue() / (double) fraction.second.intValue();
	}
	
	/**
	 * Returns the AnswerValue version of a lie, given the AngeronaAnswer containing the truth. Simply negates it. 
	 * @param truth
	 * @return	the opposite answer given as parameter (lie)
	 */
	public static AnswerValue lie(AngeronaAnswer truth)
	{
		if(truth.getAnswerValue() == AnswerValue.AV_TRUE)
			return AnswerValue.AV_FALSE;
		else if(truth.getAnswerValue() == AnswerValue.AV_FALSE)
			return AnswerValue.AV_TRUE;
		return AnswerValue.AV_UNKNOWN;
	}
	
	/**
	 * Returns the FolFormula version of a lie, wrapped in its own AngeronaAnswer. Not sure yet if it should just return the FolFormula.
	 * Simply negates it for now. Later there should be an option for lying by choosing an alternative possibility. 
	 * Lying operates on parsing the string version of the FolFormula, which is admittedly crude. 
	 * @param truth
	 * @return	The given formula is negated to create a lie from a true expression.
	 */
	public static FolFormula lie(FolFormula truth)
	{
		return (FolFormula)truth.complement();
	}
	
	public static String format(Throwable ex) {
		String reval = ex + " - " + ex.getMessage();
		while(ex.getCause() != null) {
			ex = ex.getCause();
			reval += " caused by:\n";
			reval += format(ex);
		}
		return reval;
	}
}
