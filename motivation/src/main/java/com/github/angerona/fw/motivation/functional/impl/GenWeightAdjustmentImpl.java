package com.github.angerona.fw.motivation.functional.impl;

import static com.github.angerona.fw.motivation.utils.FormatUtils.format;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.angerona.fw.motivation.GenLevelIterable;
import com.github.angerona.fw.motivation.MotiveLevel;
import com.github.angerona.fw.motivation.dao.BeliefState;
import com.github.angerona.fw.motivation.dao.LevelWeightDao;
import com.github.angerona.fw.motivation.dao.MotiveState;
import com.github.angerona.fw.motivation.functional.DeficiencyPartition;
import com.github.angerona.fw.motivation.functional.WeightAdjustment;
import com.github.angerona.fw.motivation.model.Motive;
import com.github.angerona.fw.motivation.model.MotiveCoupling;
import com.github.angerona.fw.motivation.model.WeightRange;

/**
 * 
 * @author Manuel Barbi
 * 
 * @param <L>
 */
public abstract class GenWeightAdjustmentImpl<L extends MotiveLevel> implements WeightAdjustment<L> {

	private static final Logger LOGGER = LoggerFactory.getLogger(GenWeightAdjustmentImpl.class);

	protected DeficiencyPartition<L> partition;
	protected GenLevelIterable<L> levels;

	public GenWeightAdjustmentImpl(DeficiencyPartition<L> partition, GenLevelIterable<L> levels) {
		if (partition == null) {
			throw new NullPointerException("deficiency-partition must not be null");
		}

		if (levels == null) {
			throw new NullPointerException("level-iterable must not be null");
		}

		this.partition = partition;
		this.levels = levels;
	}

	@Override
	public void adjust(MotiveState<L> ms, BeliefState b, LevelWeightDao<L> out) {

		if (ms == null) {
			throw new NullPointerException("motive-state must not be null");
		}

		if (b == null) {
			throw new NullPointerException("belief-state must not be null");
		}

		if (out == null) {
			throw new NullPointerException("level-weight-access must not be null");
		}

		WeightRange range;
		double rel_sit = rel_sit(ms, b);
		LOGGER.info("calculate reliability: {}", format(rel_sit));

		for (L level : levels) {
			if (ms.getMotives(level).size() > 0) {
				range = ms.getRange(level);
				double w;

				if (partition.contains(level)) {
					// calculate weight for deficiency levels
					w = range.getLower() + range.getDelta() * (1 - rel_sit);
				} else {
					// calculate weight for non-deficiency levels
					w = range.getLower() + range.getDelta() * rel_sit;
				}

				LOGGER.info("set level weight {} for motive level <{}>", format(w), level);
				out.putWeight(level, w);
			}
		}
	}

	public double rel_sit(MotiveState<L> ms, BeliefState b) {
		double counter = 0;
		double divider = 0;
		double mean;
		int satCount;
		Collection<Motive<L>> motives;

		for (L level : levels) {
			// get all motives related to this level
			motives = ms.getMotives(level);

			if (motives.size() > 0) {
				mean = ms.getRange(level).getMean();
				satCount = 0;

				// count satisfiable motives
				for (Motive<L> m : motives) {
					for (MotiveCoupling<L> mc : ms.getCouplings(m)) {
						// check if desire is reliable and coupled above zero
						if ((mc.getCouplingStrength() > 0) && b.isReliable(mc.getDesire())) {
							satCount++;
							break;
						}
					}
				}

				counter += mean * (satCount / motives.size());
				divider += mean;
			}
		}

		// avoid zero division
		if (divider == 0) {
			throw new IllegalStateException("agent seems to have no motives at all");
		}

		return counter / divider;
	}

}
