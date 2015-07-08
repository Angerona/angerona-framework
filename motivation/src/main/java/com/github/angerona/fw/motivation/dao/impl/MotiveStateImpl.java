package com.github.angerona.fw.motivation.dao.impl;

import java.util.Collection;

import com.github.angerona.fw.Desire;
import com.github.angerona.fw.motivation.dao.CouplingDao;
import com.github.angerona.fw.motivation.dao.LevelWeightDao;
import com.github.angerona.fw.motivation.dao.MotiveState;
import com.github.angerona.fw.motivation.dao.WeightRangeDao;
import com.github.angerona.fw.motivation.data.Motive;
import com.github.angerona.fw.motivation.data.MotiveCoupling;
import com.github.angerona.fw.motivation.data.MotiveLevel;
import com.github.angerona.fw.motivation.data.WeightRange;

/**
 * 
 * @author Manuel Barbi
 * 
 * @param <L>
 */
public class MotiveStateImpl<L extends MotiveLevel> implements MotiveState<L> {

	protected CouplingDao<L> couplings;
	protected WeightRangeDao<L> ranges;
	protected LevelWeightDao<L> weights;

	public MotiveStateImpl(CouplingDao<L> couplings, WeightRangeDao<L> ranges, LevelWeightDao<L> weights) {
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
	public Collection<MotiveCoupling<L>> getCouplings(Motive<L> m) {
		return couplings.getCouplings(m);
	}

	@Override
	public Collection<MotiveCoupling<L>> getCouplings(Desire d) {
		return couplings.getCouplings(d);
	}

	@Override
	public Collection<MotiveCoupling<L>> getCouplings() {
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
