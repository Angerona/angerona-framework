package com.github.angerona.fw.motivation.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
		String bin = Integer.toBinaryString(num);
		boolean[] bool = new boolean[len];

		for (int b = 0; b < Math.min(bin.length(), len); b++) {
			bool[b] = bin.charAt(b) == '1';
		}

		return bool;
	}

	public static final String desireToString(Desire d) {
		try {
			return d.toString();
		} catch (Exception e) {
			// ignore
		}

		return null;
	}

	public static void main(String[] args) {
		Matcher m = FORMULA.matcher("-bal_gg5, not, -foo");
		while (m.find()) {
			System.out.println(m.group(1).equals("-"));
			System.out.println(m.group(2));
		}
	}

}
