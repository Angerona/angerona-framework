package com.github.angerona.fw.motivation.functional.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
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
public class SingleDesireSelection implements DesireSelection {

	private static final MotivationComparator COMPARATOR = new MotivationComparator();

	@Override
	public Collection<Desire> select(BeliefState b, MotStructureDao st) {
		Collection<Desire> desires = new LinkedList<>();
		Set<Entry<Desire, Double>> entries = st.getEntries();

		if (!entries.isEmpty()) {
			Entry<Desire, Double> max = Collections.max(entries, COMPARATOR);
			desires.add(max.getKey());
		}

		return desires;
	}

	@Override
	public DesireSelection copy() {
		return new SingleDesireSelection();
	}

}
