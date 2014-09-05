package com.github.angerona.fw.motivation.utils;

import net.sf.tweety.logics.commons.syntax.Predicate;
import net.sf.tweety.logics.fol.syntax.FOLAtom;
import net.sf.tweety.logics.fol.syntax.FolFormula;

import com.github.angerona.fw.Desire;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class FormulaUtils {

	public static final FolFormula createFormula(String arg) {
		String fStr = arg.toLowerCase();

		// check for tautology
		if (!fStr.equals("true")) {
			// check for negation
			boolean neg = fStr.startsWith("-");
			FolFormula formula = new FOLAtom(new Predicate(fStr.substring(neg ? 1 : 0)));
			return (!neg) ? formula : (FolFormula) formula.complement();
		}

		return null;
	}

	public static final Desire createDesire(String arg) {
		return new Desire(createFormula(arg));
	}

	public static final boolean[] intToBoolAra(int num, int len) {
		String bin = Integer.toBinaryString(num);
		boolean[] bool = new boolean[len];

		for (int b = 0; b < Math.min(bin.length(), len); b++) {
			bool[b] = bin.charAt(b) == '1';
		}

		return bool;
	}

}
