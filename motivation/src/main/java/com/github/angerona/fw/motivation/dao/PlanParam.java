package com.github.angerona.fw.motivation.dao;

import java.util.Collection;

import net.sf.tweety.logics.fol.syntax.FolFormula;

import com.github.angerona.fw.motivation.island.enums.Location;
import com.github.angerona.fw.motivation.plan.StateNode;
import com.github.angerona.fw.motivation.plan.WeatherChart;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public interface PlanParam {

	public Collection<StateNode> getPlans();

	public Location getLocation();

	public WeatherChart getWeather();

	public boolean reason(FolFormula formula);

}
