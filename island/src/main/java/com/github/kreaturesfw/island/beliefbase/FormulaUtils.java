package com.github.kreaturesfw.island.beliefbase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.github.kreaturesfw.core.bdi.Desire;

import net.sf.tweety.logics.commons.syntax.Predicate;
import net.sf.tweety.logics.fol.syntax.FOLAtom;
import net.sf.tweety.logics.fol.syntax.FolFormula;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class FormulaUtils {

	private static final Pattern FORMULA = Pattern.compile("(\\-?)(\\w+)(,\\s?)?");

	public static final FolFormula createFormula(String arg) {
		Matcher m = FORMULA.matcher(arg.toLowerCase());
		FolFormula out = null;
		FolFormula f;
		String part;
		boolean neg;

		while (m.find()) {
			neg = m.group(1).equals("-");
			part = m.group(2);

			if (neg || !part.equals("true")) {
				f = new FOLAtom(new Predicate(part));

				if (out != null) {
					out = out.combineWithAnd(neg ? (FolFormula) f.complement() : f);
				} else {
					out = neg ? (FolFormula) f.complement() : f;
				}
			}
		}

		return out;
	}

	public static final Desire createDesire(String arg) {
		return new Desire(createFormula(arg));
	}

	public static final boolean[] intToBoolAra(int num, int len) {

		boolean[] bool = new boolean[len];
		int val = num;
		int pos = len;

		do {
			bool[--pos] = (val & 1) == 1;
			val >>>= 1;
		} while (val > 0 && pos > 0);

		return bool;
	}

}
