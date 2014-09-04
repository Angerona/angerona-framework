package com.github.angerona.fw.motivation.dummies;

import static com.github.angerona.fw.motivation.utils.FormulaUtils.createFormula;
import net.sf.tweety.logics.fol.syntax.FolFormula;

import com.github.angerona.fw.Desire;
import com.github.angerona.fw.motivation.dao.BeliefState;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class DummyBeliefState implements BeliefState<FolFormula> {

	public static final FolFormula WHALES = createFormula("endangered_whales");

	@Override
	public boolean isReliable(Desire d) {
		return true;
	}

	@Override
	public boolean isSatisfied(Desire d) {
		return false;
	}

	@Override
	public boolean verify(FolFormula statement) {
		return false;
	}

}
