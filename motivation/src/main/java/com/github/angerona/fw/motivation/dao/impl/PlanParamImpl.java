package com.github.angerona.fw.motivation.dao.impl;

import java.util.Collection;

import net.sf.tweety.logics.fol.syntax.FolFormula;

import com.github.angerona.fw.logic.Beliefs;
import com.github.angerona.fw.motivation.dao.PlanParam;
import com.github.angerona.fw.motivation.island.enums.Location;
import com.github.angerona.fw.motivation.plan.StateNode;
import com.github.angerona.fw.motivation.plan.WeatherChart;
import com.github.angerona.fw.motivation.utils.FolReasonUtils;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class PlanParamImpl implements PlanParam {

	protected Collection<StateNode> plans;
	protected Location location;
	protected WeatherChart weather;
	protected Beliefs beliefs;

	public PlanParamImpl(Collection<StateNode> plans, Location location, WeatherChart weather, Beliefs beliefs) {

		if (plans == null) {
			throw new NullPointerException("plans must not be null");
		}

		if (location == null) {
			throw new NullPointerException("location must not be null");
		}

		if (weather == null) {
			throw new NullPointerException("weather must not be null");
		}

		if (beliefs == null) {
			throw new NullPointerException("beliefs must not be null");
		}

		this.plans = plans;
		this.location = location;
		this.weather = weather;
		this.beliefs = beliefs;
	}

	@Override
	public Collection<StateNode> getPlans() {
		return plans;
	}

	@Override
	public Location getLocation() {
		return location;
	}

	@Override
	public WeatherChart getWeather() {
		return weather;
	}

	@Override
	public boolean reason(FolFormula formula) {
		if (formula == null) {
			return true;
		}

		return FolReasonUtils.reason(beliefs, formula);
	}

}
