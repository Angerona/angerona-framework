package com.github.kreatures.island.behavior;

import com.github.kreatures.island.enums.Weather;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class DynamicIslandBehavior extends IslandBehavior {

	@Override
	protected Weather nextWeather() {
		return randomWeather();
	}

	@Override
	protected Weather prediction(Weather next) {
		Weather alt;

		// generating weather until it's different from the actual
		do {
			alt = randomWeather();
		} while (alt == next);

		// weather prediction is right with a probability of 70%
		return GENERATOR.chance(7, 10) ? next : alt;
	}

}
