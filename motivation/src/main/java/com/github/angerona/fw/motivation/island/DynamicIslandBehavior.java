package com.github.angerona.fw.motivation.island;

import com.github.angerona.fw.motivation.island.enums.Weather;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class DynamicIslandBehavior extends IslandBehavior {

	@Override
	protected Weather generateWeather() {
		return generator.next();
	}

	@Override
	protected Weather prediction(Weather next) {
		Weather alt;

		// generating weather until it's different from the actual
		do {
			alt = generator.next();
		} while (alt == next);

		// weather prediction is right with a probability of 70%
		return generator.chance(7, 10) ? next : alt;
	}

}
