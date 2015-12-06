package com.github.kreaturesfw.core.util;

import java.security.SecureRandom;
import java.util.Random;

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
	 * @return true with a probability of k/n
	 */
	public boolean chance(int k, int n) {
		if (k < 1) {
			throw new IllegalArgumentException("k must be greater than 0");
		}

		if (k > n) {
			throw new IllegalArgumentException("k must not be greater than n");
		}

		return random.nextInt(n) < k;
	}

	/**
	 * 
	 * @param bound
	 * @return a random number smaller than bound
	 */
	public int nextInt(int bound) {
		return random.nextInt(bound);
	}

}
