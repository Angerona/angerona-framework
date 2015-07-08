package com.github.angerona.fw.motivation.functional.impl;

import java.util.Comparator;
import java.util.Map.Entry;

import com.github.angerona.fw.Desire;

/**
 * 
 * @author Manuel Barbi
 *
 */
public class MotivationComparator implements Comparator<Entry<Desire, Double>> {

	@Override
	public int compare(Entry<Desire, Double> entry1, Entry<Desire, Double> entry2) {
		return Double.compare(entry1.getValue(), entry2.getValue());
	}

}
