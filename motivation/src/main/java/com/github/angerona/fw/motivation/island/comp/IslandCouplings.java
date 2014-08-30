package com.github.angerona.fw.motivation.island.comp;

import static com.github.angerona.fw.motivation.Maslow.ESTEEM;
import static com.github.angerona.fw.motivation.Maslow.PHYSIOLOGICAL_NEEDS;
import static com.github.angerona.fw.motivation.Maslow.SAFETY_NEEDS;
import static com.github.angerona.fw.motivation.island.comp.IslandDesires.FILL_BATTERY;
import static com.github.angerona.fw.motivation.island.comp.IslandDesires.FIND_SHELTER;
import static com.github.angerona.fw.motivation.island.comp.IslandDesires.FINISH_WORK;
import static com.github.angerona.fw.motivation.island.comp.IslandDesires.SECURE_SITE;
import net.sf.tweety.logics.commons.syntax.Predicate;
import net.sf.tweety.logics.fol.syntax.FOLAtom;
import net.sf.tweety.logics.fol.syntax.FolFormula;

import com.github.angerona.fw.motivation.Maslow;
import com.github.angerona.fw.motivation.dao.impl.MotiveCouplings;
import com.github.angerona.fw.motivation.model.Motive;
import com.github.angerona.fw.motivation.model.MotiveCoupling;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class IslandCouplings extends MotiveCouplings {

	private static final Motive<Maslow> HUNGER = new Motive<>("hunger", PHYSIOLOGICAL_NEEDS);
	private static final Motive<Maslow> SELF_PRESERVATION = new Motive<>("self_preservation", SAFETY_NEEDS);
	private static final Motive<Maslow> PRUDENCE = new Motive<>("prudence", SAFETY_NEEDS);
	private static final Motive<Maslow> WORK_ETHIC = new Motive<>("work_ethic", ESTEEM);

	private static final FolFormula LOW_BATTERY = new FOLAtom(new Predicate("low_battery"));
	private static final FolFormula DANGER = new FOLAtom(new Predicate("danger"));
	private static final FolFormula TAUTOLOGY = new FOLAtom(new Predicate("tautology"));

	public IslandCouplings() {
		this.couplings.add(new MotiveCoupling<Maslow, FolFormula>(HUNGER, FILL_BATTERY, 1.0, LOW_BATTERY));
		this.couplings.add(new MotiveCoupling<Maslow, FolFormula>(PRUDENCE, FILL_BATTERY, 0.3, TAUTOLOGY));

		this.couplings.add(new MotiveCoupling<Maslow, FolFormula>(SELF_PRESERVATION, FIND_SHELTER, 0.8, DANGER));
		this.couplings.add(new MotiveCoupling<Maslow, FolFormula>(HUNGER, FIND_SHELTER, -0.3, LOW_BATTERY));

		this.couplings.add(new MotiveCoupling<Maslow, FolFormula>(PRUDENCE, SECURE_SITE, 0.9, TAUTOLOGY));

		this.couplings.add(new MotiveCoupling<Maslow, FolFormula>(WORK_ETHIC, FINISH_WORK, 0.85, TAUTOLOGY));
		this.couplings.add(new MotiveCoupling<Maslow, FolFormula>(HUNGER, FINISH_WORK, -0.7, LOW_BATTERY));
		this.couplings.add(new MotiveCoupling<Maslow, FolFormula>(SELF_PRESERVATION, FINISH_WORK, -0.3, DANGER));
	}

}
