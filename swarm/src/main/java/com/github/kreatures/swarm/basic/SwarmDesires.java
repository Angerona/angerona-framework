package com.github.kreatures.swarm.basic;

import net.sf.tweety.logics.commons.syntax.Predicate;
import net.sf.tweety.logics.fol.syntax.FOLAtom;

import com.github.kreatures.core.Desire;

/**
 * 
 * @author donfack
 *
 */
public class SwarmDesires {
	
	
	/**
	 * A agent has selected a station as next destination.
	 */
	public static final Desire WANT_TO_LEAVE = new Desire(new FOLAtom(new Predicate("want_to_move")));
	/**
	 * A agent is located at a station and want to visit it.
	 */
	public static final Desire WANT_TO_VISIT = new Desire(new FOLAtom(new Predicate("want_to_visit")));
	/**
	 * A agent is visiting a station and can do its jobs on that station.
	 */
	public static final Desire IS_VISIT = new Desire(new FOLAtom(new Predicate("is_visit")));
	/**
	 * A agent is on the way to a station.
	 */
	public static final Desire WANT_TO_MOVE = new Desire(new FOLAtom(new Predicate("is_on_way")));

	/**
	 * @author Manuel Barbi
	 * @param fst
	 * @param snd
	 * @return
	 */
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
