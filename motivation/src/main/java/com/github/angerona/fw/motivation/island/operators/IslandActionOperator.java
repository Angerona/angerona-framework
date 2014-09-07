package com.github.angerona.fw.motivation.island.operators;

import com.github.angerona.fw.motivation.dao.PlanComponentDao;
import com.github.angerona.fw.motivation.dao.impl.GenTrailBasedPlans;
import com.github.angerona.fw.motivation.operators.ActionSelectionParameter;
import com.github.angerona.fw.motivation.operators.BaseActionSelectionOperator;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class IslandActionOperator extends BaseActionSelectionOperator {

	@Override
	protected Void processImpl(ActionSelectionParameter param) {
		PlanComponentDao plans = param.getAgent().getComponent(GenTrailBasedPlans.class);
		return null;
	}

}
