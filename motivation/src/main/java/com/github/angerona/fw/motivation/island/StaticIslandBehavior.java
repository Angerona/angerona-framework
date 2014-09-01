package com.github.angerona.fw.motivation.island;

import com.github.angerona.fw.motivation.island.enums.Weather;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class StaticIslandBehavior extends IslandBehavior {

	@Override
	protected Weather generateWeather() {
		return Weather.CLOUDS;
	}

	@Override
	protected Weather prediction(Weather next) {
		return next;
	}

}
