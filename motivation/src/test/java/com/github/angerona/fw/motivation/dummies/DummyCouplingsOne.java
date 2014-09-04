package com.github.angerona.fw.motivation.dummies;

import static com.github.angerona.fw.motivation.Maslow.ESTEEM;
import static com.github.angerona.fw.motivation.Maslow.SELF_ACTUALIZATION;
import static com.github.angerona.fw.motivation.dummies.DummyBeliefState.WHALES;
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
public class DummyCouplingsOne extends MotiveCouplings {

	public static final Motive<Maslow> ENV_AWARENESS = new Motive<>("env_awareness", SELF_ACTUALIZATION);
	public static final Motive<Maslow> PRESTIGE = new Motive<>("prestige", ESTEEM);

	public static final Desire SAVE_WHALES = createDesire("save_whales");
	public static final Desire BUY_LOCAL_FRUITS = createDesire("buy_local_fruits");
	public static final Desire BUY_SPORTS_CAR = createDesire("buy_sports_car");

	public static final MotiveCoupling<Maslow, FolFormula> COUPL_WHALES = new MotiveCoupling<>(ENV_AWARENESS, SAVE_WHALES, 0.9, WHALES);
	public static final MotiveCoupling<Maslow, FolFormula> COUPL_FRUITS = new MotiveCoupling<>(ENV_AWARENESS, BUY_LOCAL_FRUITS, 0.3, null);
	public static final MotiveCoupling<Maslow, FolFormula> COUPL_AWARE = new MotiveCoupling<>(ENV_AWARENESS, BUY_SPORTS_CAR, -0.9, null);
	public static final MotiveCoupling<Maslow, FolFormula> COUPL_PRESTIGE = new MotiveCoupling<>(PRESTIGE, BUY_SPORTS_CAR, 1, null);

	public DummyCouplingsOne() {
		this.couplings.add(COUPL_WHALES);
		this.couplings.add(COUPL_FRUITS);
		this.couplings.add(COUPL_AWARE);
		this.couplings.add(COUPL_PRESTIGE);
	}
}
