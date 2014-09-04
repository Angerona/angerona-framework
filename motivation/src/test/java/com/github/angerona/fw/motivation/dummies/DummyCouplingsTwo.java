package com.github.angerona.fw.motivation.dummies;

import static com.github.angerona.fw.motivation.Maslow.PHYSIOLOGICAL_NEEDS;
import static com.github.angerona.fw.motivation.Maslow.SELF_ACTUALIZATION;
import static com.github.angerona.fw.motivation.utils.FormulaUtils.createDesire;
import net.sf.tweety.logics.fol.syntax.FolFormula;

import com.github.angerona.fw.Desire;
import com.github.angerona.fw.motivation.Maslow;
import com.github.angerona.fw.motivation.dao.impl.MotiveCouplings;
import com.github.angerona.fw.motivation.model.Motive;
import com.github.angerona.fw.motivation.model.MotiveCoupling;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class DummyCouplingsTwo extends MotiveCouplings {

	public static final Motive<Maslow> ENVIRONMENTAL_AWARENESS = new Motive<>("environmental awareness", SELF_ACTUALIZATION);
	public static final Motive<Maslow> FOOD = new Motive<>("food", PHYSIOLOGICAL_NEEDS);

	public static final Desire OWN_COUNTRY = createDesire("buy fruits from your own country");
	public static final Desire CHEAP_FOOD = createDesire("buy cheap food");

	public static final MotiveCoupling<Maslow, FolFormula> COUPL_OWN = new MotiveCoupling<>(ENVIRONMENTAL_AWARENESS, OWN_COUNTRY, 0.3, null);
	public static final MotiveCoupling<Maslow, FolFormula> COUPL_CHEAP = new MotiveCoupling<>(FOOD, CHEAP_FOOD, 0.3, null);

	public DummyCouplingsTwo() {
		this.couplings.add(COUPL_OWN);
		this.couplings.add(COUPL_CHEAP);
	}
}
