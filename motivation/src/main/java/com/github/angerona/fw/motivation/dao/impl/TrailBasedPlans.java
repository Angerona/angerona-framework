package com.github.angerona.fw.motivation.dao.impl;

import com.github.angerona.fw.BaseAgentComponent;
import com.github.angerona.fw.motivation.dao.PlanDao;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class TrailBasedPlans extends BaseAgentComponent implements PlanDao {

	@Override
	public BaseAgentComponent clone() {
		TrailBasedPlans cln = new TrailBasedPlans();
		return cln;
	}

}
