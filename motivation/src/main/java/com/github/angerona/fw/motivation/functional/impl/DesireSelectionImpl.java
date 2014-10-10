package com.github.angerona.fw.motivation.functional.impl;

import java.util.Collection;
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

	@Override
	public Collection<Desire> select(BeliefState b, MotStructureDao st) {
		Set<Desire> desires = new HashSet<Desire>();

		Double mu;
		for (Entry<Desire, Double> entry : st.getEntries()) {
			mu = entry.getValue();

			if (mu != null && mu > 0) {
				desires.add(entry.getKey());
			}
		}

		return desires;
	}

	@Override
	public DesireSelection copy() {
		return new DesireSelectionImpl();
	}

}
