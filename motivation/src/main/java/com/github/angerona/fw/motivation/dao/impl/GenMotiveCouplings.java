package com.github.angerona.fw.motivation.dao.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.github.angerona.fw.BaseAgentComponent;
import com.github.angerona.fw.Desire;
import com.github.angerona.fw.motivation.Motive;
import com.github.angerona.fw.motivation.MotiveCoupling;
import com.github.angerona.fw.motivation.MotiveLevel;
import com.github.angerona.fw.motivation.dao.CouplingDao;

/**
 * {@link GenMotiveCouplings} is a generic class delivering access to the MotiveCouplings of an Agent. MotiveCouplings can be parsed from file.
 * 
 * @author Manuel Barbi
 * 
 */
public abstract class GenMotiveCouplings<L extends MotiveLevel> extends ParsableComponent implements CouplingDao<L>, Iterable<MotiveCoupling<L>> {

	protected Set<MotiveCoupling<L>> couplings;

	public GenMotiveCouplings() {
		this(new HashSet<MotiveCoupling<L>>());
	}

	public GenMotiveCouplings(Set<MotiveCoupling<L>> couplings) {
		if (couplings == null) {
			throw new NullPointerException("couplings must not be null");
		}

		this.couplings = couplings;
	}

	@Override
	public Collection<Motive<L>> getMotives(L level) {
		Set<Motive<L>> mot = new HashSet<>();

		for (MotiveCoupling<L> mc : couplings) {
			if (mc.getMotive().getLevel() == level) {
				mot.add(mc.getMotive());
			}
		}

		return mot;
	}

	@Override
	public Collection<Motive<L>> getMotives() {
		Set<Motive<L>> mot = new HashSet<>();

		for (MotiveCoupling<L> mc : couplings) {
			mot.add(mc.getMotive());
		}

		return mot;
	}

	@Override
	public Collection<Desire> getDesires() {
		Set<Desire> des = new HashSet<>();

		for (MotiveCoupling<L> mc : couplings) {
			des.add(mc.getDesire());
		}

		return des;
	}

	@Override
	public Collection<MotiveCoupling<L>> getCouplings(Motive<L> m) {
		Set<MotiveCoupling<L>> cpl = new HashSet<>();

		for (MotiveCoupling<L> mc : couplings) {
			if (mc.getMotive().equals(m)) {
				cpl.add(mc);
			}
		}

		return cpl;
	}

	@Override
	public Collection<MotiveCoupling<L>> getCouplings(Desire d) {
		Set<MotiveCoupling<L>> cpl = new HashSet<>();

		for (MotiveCoupling<L> mc : couplings) {
			if (mc.getDesire().equals(d)) {
				cpl.add(mc);
			}
		}

		return cpl;
	}

	@Override
	public Collection<MotiveCoupling<L>> getCouplings() {
		return Collections.unmodifiableSet(couplings);
	}

	@Override
	public double getMeanCouplingStrength() {
		double sum = 0;
		int count = 0;

		for (MotiveCoupling<L> mc : couplings) {
			if (mc.getCouplingStrength() > 0) {
				sum += mc.getCouplingStrength();
				count++;
			}
		}

		return (count > 0) ? (sum / count) : 0;
	}

	@Override
	public Iterator<MotiveCoupling<L>> iterator() {
		return couplings.iterator();
	}

	@Override
	public BaseAgentComponent clone() {
		GenMotiveCouplings<L> cln = create();
		cln.couplings.addAll(this.couplings);
		return cln;
	}

	protected abstract GenMotiveCouplings<L> create();

}
