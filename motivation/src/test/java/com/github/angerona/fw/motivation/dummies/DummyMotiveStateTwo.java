package com.github.angerona.fw.motivation.dummies;

import net.sf.tweety.logics.fol.syntax.FolFormula;

import com.github.angerona.fw.motivation.Maslow;
import com.github.angerona.fw.motivation.dao.impl.MotiveStateImpl;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class DummyMotiveStateTwo extends MotiveStateImpl<Maslow, FolFormula> {

	public DummyMotiveStateTwo() {
		super(new DummyCouplingsTwo(), new DummyRanges(), new DummyWeightsTwo());
	}

}
