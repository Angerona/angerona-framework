package com.github.angerona.fw.motivation.functional.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
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
	public Collection<Desire> select(BeliefState b, MotStructureDao st) {
		Set<Desire> desires = new HashSet<Desire>();
		Set<Entry<Desire, Double>> entries = st.getEntries();

		if (!entries.isEmpty()) {
			// TODO: add more than one Desire if they have equal motivation-values
			desires.add(Collections.max(entries, COMPARATOR).getKey());
		}

		return desires;
	}

	@Override
	public DesireSelection copy() {
		return new DesireSelectionImpl();
	}

}
