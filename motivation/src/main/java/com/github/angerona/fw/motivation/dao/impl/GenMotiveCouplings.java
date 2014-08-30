package com.github.angerona.fw.motivation.dao.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import net.sf.tweety.Formula;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.angerona.fw.BaseAgentComponent;
import com.github.angerona.fw.Desire;
import com.github.angerona.fw.motivation.MotiveLevel;
import com.github.angerona.fw.motivation.dao.CouplingDao;
import com.github.angerona.fw.motivation.model.Motive;
import com.github.angerona.fw.motivation.model.MotiveCoupling;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public abstract class GenMotiveCouplings<L extends MotiveLevel, F extends Formula> extends BaseAgentComponent implements CouplingDao<L, F> {

	private static final Logger LOG = LoggerFactory.getLogger(GenMotiveCouplings.class);

	protected Set<MotiveCoupling<L, F>> couplings;

	public GenMotiveCouplings() {
		this(new HashSet<MotiveCoupling<L, F>>());
	}

	public GenMotiveCouplings(Set<MotiveCoupling<L, F>> couplings) {
		if (couplings == null) {
			throw new NullPointerException("couplings must not be null");
		}

		this.couplings = couplings;

		LOG.debug("created {}", this.getClass().getSimpleName());
	}

	@Override
	public Collection<Motive<L>> getMotives(L level) {
		Collection<Motive<L>> mot = new LinkedList<>();

		for (MotiveCoupling<L, F> mc : couplings) {
			if (mc.getMotive().getLevel() == level) {
				mot.add(mc.getMotive());
			}
		}

		return mot;
	}

	@Override
	public Collection<Motive<L>> getMotives() {
		Set<Motive<L>> mot = new HashSet<>();

		for (MotiveCoupling<L, F> mc : couplings) {
			mot.add(mc.getMotive());
		}

		return mot;
	}

	@Override
	public Collection<Desire> getDesires() {
		Set<Desire> des = new HashSet<>();

		for (MotiveCoupling<L, F> mc : couplings) {
			des.add(mc.getDesire());
		}

		return des;
	}

	@Override
	public Collection<MotiveCoupling<L, F>> getCouplings(Motive<L> m) {
		Set<MotiveCoupling<L, F>> cpl = new HashSet<>();

		for (MotiveCoupling<L, F> mc : couplings) {
			if (mc.getMotive().equals(m)) {
				cpl.add(mc);
			}
		}

		return couplings;
	}

	@Override
	public Collection<MotiveCoupling<L, F>> getCouplings(Desire d) {
		Set<MotiveCoupling<L, F>> cpl = new HashSet<>();

		for (MotiveCoupling<L, F> mc : couplings) {
			if (mc.getDesire().equals(d)) {
				cpl.add(mc);
			}
		}

		return couplings;
	}

	@Override
	public Collection<MotiveCoupling<L, F>> getCouplings() {
		return Collections.unmodifiableSet(couplings);
	}

	@Override
	public double getMeanCouplingStrength() {
		int sum = 0;
		int count = 0;

		for (MotiveCoupling<L, F> mc : couplings) {
			if (mc.getCouplingStrength() > 0) {
				sum += mc.getCouplingStrength();
				count++;
			}
		}

		return (count > 0) ? sum / count : 0;
	}

	@Override
	public BaseAgentComponent clone() {
		GenMotiveCouplings<L, F> cln = create();
		cln.couplings.addAll(this.couplings);
		return cln;
	}

	protected abstract GenMotiveCouplings<L, F> create();

}
