package com.github.kreatures.island.data;

import net.sf.tweety.logics.commons.syntax.Predicate;
import net.sf.tweety.logics.fol.syntax.FOLAtom;

import com.github.kreatures.core.Desire;

/**
 * 
 * @author Manuel Barbi
 *
 */
public class IslandDesires {

	public static final Desire FINISH_WORK = new Desire(new FOLAtom(new Predicate("finish_work")));
	public static final Desire FILL_BATTERY = new Desire(new FOLAtom(new Predicate("fill_battery")));
	public static final Desire FIND_SHELTER = new Desire(new FOLAtom(new Predicate("find_shelter")));
	public static final Desire SECURE_SITE = new Desire(new FOLAtom(new Predicate("secure_site")));

	public static final boolean isEqual(Desire fst, Desire snd) {
		if (fst == null && snd == null) {
			return true;
		}

		if (fst != null && snd != null) {
			if (fst.getFormula() == null && snd.getFormula() == null) {
				return true;
			}

			if (fst.getFormula() != null && snd.getFormula() != null) {
				return fst.getFormula().toString().equalsIgnoreCase(snd.getFormula().toString());
			}
		}

		return false;
	}

}
