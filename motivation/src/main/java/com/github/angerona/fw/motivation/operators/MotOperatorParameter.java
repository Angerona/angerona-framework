package com.github.angerona.fw.motivation.operators;

import net.sf.tweety.logics.fol.syntax.FolFormula;

import com.github.angerona.fw.motivation.Maslow;
import com.github.angerona.fw.motivation.dao.BeliefState;
import com.github.angerona.fw.motivation.dao.impl.BeliefStateImpl;
import com.github.angerona.fw.motivation.dao.impl.GenLevelWeights;
import com.github.angerona.fw.motivation.dao.impl.GenMotiveCouplings;
import com.github.angerona.fw.motivation.dao.impl.GenWeightRanges;
import com.github.angerona.fw.motivation.dao.impl.LevelWeights;
import com.github.angerona.fw.motivation.dao.impl.MotiveCouplings;
import com.github.angerona.fw.motivation.dao.impl.WeightRanges;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class MotOperatorParameter extends GenMotOperatorParameter<Maslow, FolFormula> {

	@Override
	protected GenMotiveCouplings<Maslow, FolFormula> couplings() {
		return getAgent().getComponent(MotiveCouplings.class);
	}

	@Override
	protected GenWeightRanges<Maslow> ranges() {
		return getAgent().getComponent(WeightRanges.class);
	}

	@Override
	protected GenLevelWeights<Maslow> weights() {
		return getAgent().getComponent(LevelWeights.class);
	}

	@Override
	protected BeliefState<FolFormula> beliefState() {
		return new BeliefStateImpl(getAgent().getBeliefs());
	}

}
