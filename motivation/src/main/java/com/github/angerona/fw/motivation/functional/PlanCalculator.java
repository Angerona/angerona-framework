package com.github.angerona.fw.motivation.functional;

import com.github.angerona.fw.motivation.dao.PlanParam;
import com.github.angerona.fw.motivation.plan.ActionSequence;

/**
 * 
 * @author Manuel Barbi
 * 
 * @param <ID>
 */
public interface PlanCalculator<ID extends Comparable<ID>> {

	public ActionSequence<ID> calc(PlanParam param);

}
