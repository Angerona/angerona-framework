package com.github.angerona.fw.motivation.island.comp;

import net.sf.tweety.logics.commons.syntax.Predicate;
import net.sf.tweety.logics.fol.syntax.FOLAtom;

import com.github.angerona.fw.Desire;
import com.github.angerona.fw.logic.Desires;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class IslandDesires extends Desires {

	static final Desire FILL_BATTERY = new Desire(new FOLAtom(new Predicate("fill_battery")));
	static final Desire FIND_SHELTER = new Desire(new FOLAtom(new Predicate("find_shelter")));
	static final Desire SECURE_SITE = new Desire(new FOLAtom(new Predicate("secure_site")));
	static final Desire FINISH_WORK = new Desire(new FOLAtom(new Predicate("finish_work")));

	public IslandDesires() {
		this.add(FILL_BATTERY);
		this.add(FIND_SHELTER);
		this.add(SECURE_SITE);
		this.add(FINISH_WORK);
	}

}
