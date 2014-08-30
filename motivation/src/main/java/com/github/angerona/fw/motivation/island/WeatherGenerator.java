package com.github.angerona.fw.motivation.island;

import static com.github.angerona.fw.motivation.island.enums.Weather.CLOUDS;
import static com.github.angerona.fw.motivation.island.enums.Weather.RAIN;
import static com.github.angerona.fw.motivation.island.enums.Weather.STORM;
import static com.github.angerona.fw.motivation.island.enums.Weather.SUN;
import static com.github.angerona.fw.motivation.island.enums.Weather.TEMPEST;

import java.security.SecureRandom;
import java.util.Random;

import com.github.angerona.fw.motivation.island.enums.Weather;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class WeatherGenerator {

	private final Random random = new SecureRandom();

	public WeatherGenerator() {
		random.setSeed(System.nanoTime());
	}

	public Weather generate() {
		switch (random.nextInt(5) + 1) {
		case 1:
			return SUN;
		case 2:
			return RAIN;
		case 3:
			return STORM;
		case 4:
			return TEMPEST;
		default:
			return CLOUDS;
		}
	}

	public Weather prediction(Weather weather) {
		Weather alternative = generate();
		int num = random.nextInt(8) + 1;

		if ((num % 2 == 0) && (num % 3 != 0)) {
			return alternative;
		}

		return weather;
	}

	public boolean isLightning() {
		return random.nextInt(3) == 2;
	}

	public boolean damageAgent() {
		return random.nextInt(2) == 1;
	}

	public int damageCount() {
		return random.nextInt(3) + 1;
	}

	public int damagePart() {
		return random.nextInt(8);
	}

}
