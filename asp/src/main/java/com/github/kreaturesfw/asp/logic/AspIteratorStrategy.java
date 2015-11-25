package com.github.kreaturesfw.asp.logic;

import com.github.kreaturesfw.core.operators.ContinuousBeliefOperatorFamilyIteratorStrategy;
import com.github.kreaturesfw.core.operators.OperatorCallWrapper;
import com.github.kreaturesfw.core.util.Pair;
import com.github.kreaturesfw.core.util.Utility;

/**
 * Implements a belief operator family iterator for the continuous belief
 * operator family that is defined by the AspReasoner using the parameter
 * 'd'.
 * 
 * The iterator strategy assumes that there are no more than MAX_DENOMINATOR answer sets
 * possible. It finds the next d parameter in an intelligent manner, therefore
 * it converts 'd' in a fraction and extends the fraction to the highest possible
 * denominator. If the MAX_DENOMINATOR is set to 10 and the current operator uses a
 * parameter 'd' of 1/3 then the fraction is extended to 3/9 and this fraction is used
 * to increase/decrease the parameter d, such that the next parameter 'd' becomes
 * either 4/9 or 2/9 if the successor or the predecessor is searched.
 * 
 * @author Tim Janus
 */
public class AspIteratorStrategy extends
		ContinuousBeliefOperatorFamilyIteratorStrategy {

	/** attribute defining the size of the maximum denominator */
	public static final int MAX_DENOMINATOR = 10;
	
	@Override
	public OperatorCallWrapper getPredecessor(OperatorCallWrapper current) {
		Pair<Integer, Integer> frac = getParameterAsFraction(current);
		frac.first += 1;
		OperatorCallWrapper reval = new OperatorCallWrapper(current.getImplementation());
		reval.putSetting("d", String.valueOf(Utility.fractionToDecimal(frac)));
		return reval;
	}

	@Override
	public OperatorCallWrapper getSuccessor(OperatorCallWrapper current) {
		Pair<Integer, Integer> frac = getParameterAsFraction(current);
		frac.first -= 1;
		OperatorCallWrapper reval = new OperatorCallWrapper(current.getImplementation());
		reval.putSetting("d", String.valueOf(Utility.fractionToDecimal(frac)));
		return reval;
	}

	/**
	 * Converts the parameter 'd' of the given {@link OperatorCallWrapper} ocw into a
	 * fraction and extends this fraction so that the denominator is as near as possible
	 * to MAX_DENOMINATOR.
	 * @param ocw	The {@link OperatorCallWrapper} that contains the parameter 'd'.
	 * @return		A fraction representation of the parameter 'd' of ocw, that has
	 * 				a denominator that is near to MAX_DENOMINATOR.
	 */
	private Pair<Integer, Integer> getParameterAsFraction(OperatorCallWrapper ocw) {
		double decimalParameter = Double.parseDouble(ocw.getSetting("d", "1"));
		Pair<Integer, Integer> frac = Utility.decimalToFraction(decimalParameter);
		
		// initialize candidate with maximum denominator:
		int denominatorCandidate = MAX_DENOMINATOR;
		
		// find the highest possible denominator in range (frac.second, MAX_DENOMINATOR)
		while(denominatorCandidate > frac.second) {
			if(denominatorCandidate % frac.second == 0) {
				int factor = denominatorCandidate / frac.second;
				frac.first *= factor;
				frac.second *= factor;
				break;
			} else {
				denominatorCandidate-=1;
			}
		}
		return frac;
	}
}
