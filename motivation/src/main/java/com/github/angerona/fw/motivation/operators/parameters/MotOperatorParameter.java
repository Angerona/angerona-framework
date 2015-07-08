package com.github.angerona.fw.motivation.operators.parameters;

import com.github.angerona.fw.motivation.dao.impl.GenLevelWeights;
import com.github.angerona.fw.motivation.dao.impl.GenMotiveCouplings;
import com.github.angerona.fw.motivation.dao.impl.GenWeightRanges;
import com.github.angerona.fw.motivation.dao.impl.LevelWeights;
import com.github.angerona.fw.motivation.dao.impl.MotiveCouplings;
import com.github.angerona.fw.motivation.dao.impl.WeightRanges;
import com.github.angerona.fw.motivation.data.Maslow;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class MotOperatorParameter extends GenMotOperatorParameter<Maslow> {

	@Override
	protected GenMotiveCouplings<Maslow> couplings() {
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

}
