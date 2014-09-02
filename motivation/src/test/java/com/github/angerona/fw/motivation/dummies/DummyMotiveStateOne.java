package com.github.angerona.fw.motivation.dummies;

import net.sf.tweety.logics.fol.syntax.FolFormula;

import com.github.angerona.fw.motivation.Maslow;
import com.github.angerona.fw.motivation.dao.impl.MotiveStateImpl;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class DummyMotiveStateOne extends MotiveStateImpl<Maslow, FolFormula> {

	public DummyMotiveStateOne() {
		super(new DummyCouplingsOne(), new DummyRanges(), new DummyWeightsOne());
	}

}
