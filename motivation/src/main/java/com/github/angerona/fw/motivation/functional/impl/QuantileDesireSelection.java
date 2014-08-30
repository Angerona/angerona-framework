package com.github.angerona.fw.motivation.functional.impl;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import net.sf.tweety.Formula;

import com.github.angerona.fw.Desire;
import com.github.angerona.fw.motivation.dao.BeliefState;
import com.github.angerona.fw.motivation.dao.MotStructureDao;
import com.github.angerona.fw.motivation.functional.DesireSelection;
import com.github.angerona.fw.motivation.model.MotStrcEntry;

public class QuantileDesireSelection<F extends Formula> implements DesireSelection<F> {

	public static final double UPPER_QUARTILE = 0.75;
	protected final double p;

	public QuantileDesireSelection() {
		this(UPPER_QUARTILE);
	}

	public QuantileDesireSelection(double p) {
		if (!(p > 0) || !(p < 1)) {
			throw new IllegalArgumentException("p must lie exclusive between 0 and 1");
		}

		this.p = p;
	}

	@Override
	public Collection<Desire> select(BeliefState<F> b, MotStructureDao st) {
		List<MotStrcEntry> entries = st.getOrderedEntries();
		List<Desire> des = new LinkedList<>();
		Double xp = calcQuantile(entries, p);

		if (xp != null) {
			for (MotStrcEntry e : entries) {
				if (e.getMotivationalValue() >= xp) {
					des.add(e.getDesire());
				}
			}
		}

		return des;
	}

	Double calcQuantile(List<MotStrcEntry> entries, double p) {
		if (entries == null) {
			throw new NullPointerException("entries must not be null");
		}

		if (entries.isEmpty()) {
			return null;
		}

		double np = entries.size() * p;
		double cnp = Math.ceil(np);

		// check if n*p is a whole number
		if (np != cnp) {
			return entries.get((int) cnp - 1).getMotivationalValue();
		} else {
			return (entries.get((int) np - 1).getMotivationalValue() + entries.get((int) np).getMotivationalValue()) / 2;
		}
	}

	@Override
	public DesireSelection<F> copy() {
		return new QuantileDesireSelection<>(p);
	}

}
