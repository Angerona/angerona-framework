package com.github.angerona.fw.motivation.dao.impl;

import static com.github.angerona.fw.motivation.utils.FormulaUtils.desireToString;

import java.util.Collection;
import java.util.Map;

import net.sf.tweety.Formula;

import com.github.angerona.fw.BaseAgentComponent;
import com.github.angerona.fw.Desire;
import com.github.angerona.fw.motivation.dao.PlanComponentDao;
import com.github.angerona.fw.motivation.plan.StateNode;

/**
 * 
 * @author Manuel Barbi
 * 
 * @param <ID>
 * @param <F>
 */
public abstract class GenTrailBasedPlans<ID extends Comparable<ID>, F extends Formula> extends ParsableComponent implements
		PlanComponentDao<ID, F> {

	protected Map<String, Collection<StateNode<ID, F>>> trails;

	@Override
	public Collection<StateNode<ID, F>> getPlan(Desire d) {
		String key = desireToString(d);

		if (key != null) {
			return trails.get(key);
		}

		return null;
	}

	protected abstract GenTrailBasedPlans<ID, F> create();

	@Override
	public BaseAgentComponent clone() {
		GenTrailBasedPlans<ID, F> cln = create();
		return cln;
	}

}
