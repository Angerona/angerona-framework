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
		return new FOLAtom(new Predicate(arg));
	}

	public static final Desire createDesire(String arg) {
		return new Desire(createFormula(arg));
	}

}
