package com.github.angerona.fw.motivation.dao.impl;

import java.util.Collection;

import net.sf.tweety.Formula;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.angerona.fw.Desire;
import com.github.angerona.fw.motivation.MotiveLevel;
import com.github.angerona.fw.motivation.dao.CouplingDao;
import com.github.angerona.fw.motivation.dao.LevelWeightDao;
import com.github.angerona.fw.motivation.dao.MotiveState;
import com.github.angerona.fw.motivation.dao.WeightRangeDao;
import com.github.angerona.fw.motivation.model.Motive;
import com.github.angerona.fw.motivation.model.MotiveCoupling;
import com.github.angerona.fw.motivation.model.WeightRange;

/**
 * 
 * @author Manuel Barbi
 * 
 * @param <L>
 * @param <F>
 */
public class MotiveStateImpl<L extends MotiveLevel, F extends Formula> implements MotiveState<L, F> {

	private static final Logger LOG = LoggerFactory.getLogger(MotiveStateImpl.class);

	protected CouplingDao<L, F> couplings;
	protected WeightRangeDao<L> ranges;
	protected LevelWeightDao<L> weights;

	public MotiveStateImpl(CouplingDao<L, F> couplings, WeightRangeDao<L> ranges, LevelWeightDao<L> weights) {
		if (couplings == null) {
			throw new NullPointerException("coupling-dao must not be null");
		}

		if (ranges == null) {
			throw new NullPointerException("weight-range-dao must not be null");
		}

		if (weights == null) {
			throw new NullPointerException("level-weight-dao must not be null");
		}

		this.couplings = couplings;
		this.ranges = ranges;
		this.weights = weights;

		LOG.debug("created {}", this.getClass().getSimpleName());
	}

	@Override
	public Collection<Motive<L>> getMotives(L level) {
		return couplings.getMotives(level);
	}

	@Override
	public Collection<Motive<L>> getMotives() {
		return couplings.getMotives();
	}

	@Override
	public Collection<Desire> getDesires() {
		return couplings.getDesires();
	}

	@Override
	public Collection<MotiveCoupling<L, F>> getCouplings(Motive<L> m) {
		return couplings.getCouplings(m);
	}

	@Override
	public Collection<MotiveCoupling<L, F>> getCouplings(Desire d) {
		return couplings.getCouplings(d);
	}

	@Override
	public Collection<MotiveCoupling<L, F>> getCouplings() {
		return couplings.getCouplings();
	}

	@Override
	public double getMeanCouplingStrength() {
		return couplings.getMeanCouplingStrength();
	}

	@Override
	public WeightRange getRange(L level) {
		return ranges.getRange(level);
	}

	@Override
	public double getWeight(L level) {
		return weights.getWeight(level);
	}

	@Override
	public void putWeight(L level, double w) {
		weights.putWeight(level, w);
	}

}
