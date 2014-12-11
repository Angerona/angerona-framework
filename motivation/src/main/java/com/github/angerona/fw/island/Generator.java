package com.github.angerona.fw.island;

import java.security.SecureRandom;
import java.util.Random;

import com.github.angerona.fw.island.enums.Weather;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class Generator {

	protected final Random random = new SecureRandom();

	public Generator() {
		random.setSeed(System.nanoTime());
	}

	/**
	 * 
	 * @param k
	 * @param n
	 * @return true with a probability of n/k
	 */
	public boolean chance(int k, int n) {
		if (n < 1) {
			throw new IllegalArgumentException("n must be greater than 0");
		}

		if (k > n) {
			throw new IllegalArgumentException("k must not be greater than n");
		}

		return random.nextInt(n) < k;
	}

	/**
	 * 
	 * @return a random weather occurrence
	 */
	public Weather next() {
		switch (random.nextInt(4)) {
		case 1:
			return Weather.SUN;
		case 2:
			return Weather.STORM_OR_RAIN;
		case 3:
			return Weather.THUNDERSTORM;
		default:
			return Weather.CLOUDS;
		}
	}
}
