package com.github.angerona.fw.motivation.functional.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.Map.Entry;
import java.util.Set;

import com.github.angerona.fw.Desire;
import com.github.angerona.fw.motivation.dao.BeliefState;
import com.github.angerona.fw.motivation.dao.MotStructureDao;
import com.github.angerona.fw.motivation.functional.DesireSelection;

/**
 * 
 * @author Manuel Barbi
 *
 */
public class DesireSelectionImpl implements DesireSelection {

	protected static final Comparator<Entry<Desire, Double>> COMPARATOR = new MotivationComparator();

	@Override
	public Desire select(BeliefState b, MotStructureDao st) {
		Set<Entry<Desire, Double>> entries = st.getEntries();

		if (!entries.isEmpty()) {
			return Collections.max(entries, COMPARATOR).getKey();
		}

		return null;
	}

	@Override
	public DesireSelection copy() {
		return new DesireSelectionImpl();
	}

}
