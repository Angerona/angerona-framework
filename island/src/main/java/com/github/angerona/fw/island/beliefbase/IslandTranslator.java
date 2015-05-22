package com.github.angerona.fw.island.beliefbase;

import net.sf.tweety.lp.nlp.syntax.NLPProgram;

import com.github.angerona.fw.BaseBeliefbase;
import com.github.angerona.fw.Perception;
import com.github.angerona.fw.logic.BaseTranslator;

public class IslandTranslator extends BaseTranslator {

	@Override
	protected BaseBeliefbase translatePerceptionImpl(BaseBeliefbase caller, Perception p) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected BaseBeliefbase translateNLPImpl(BaseBeliefbase caller, NLPProgram program) {
		throw new UnsupportedOperationException("translateNLPImpl");
	}

	@Override
	protected BaseBeliefbase defaultReturnValue() {
		// TODO Auto-generated method stub
		return null;
	}

}
