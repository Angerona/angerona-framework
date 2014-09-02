package com.github.angerona.fw.motivation.functional.impl;

import static com.github.angerona.fw.motivation.utils.FormatUtils.format;

import java.util.Collection;
import java.util.HashSet;

import net.sf.tweety.Formula;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.angerona.fw.Desire;
import com.github.angerona.fw.motivation.MotiveLevel;
import com.github.angerona.fw.motivation.dao.BeliefState;
import com.github.angerona.fw.motivation.dao.MotStructureDao;
import com.github.angerona.fw.motivation.dao.MotiveState;
import com.github.angerona.fw.motivation.functional.AggregationFunction;
import com.github.angerona.fw.motivation.functional.StructureAdjustment;
import com.github.angerona.fw.motivation.model.MotStrcEntry;
import com.github.angerona.fw.motivation.model.MotiveCoupling;

/**
 * 
 * @author Manuel Barbi
 * 
 * @param <L>
 * @param <F>
 */
public abstract class GenStrcAdjustmentImpl<L extends MotiveLevel, F extends Formula> implements StructureAdjustment<L, F> {

	private static final Logger LOGGER = LoggerFactory.getLogger(GenStrcAdjustmentImpl.class);

	protected AggregationFunction aggregation;

	public GenStrcAdjustmentImpl(AggregationFunction aggregation) {
		if (aggregation == null) {
			throw new NullPointerException("aggregation-function must not be null");
		}

		this.aggregation = aggregation;
	}

	@Override
	public void adjust(MotiveState<L, F> ms, BeliefState<F> b, MotStructureDao out) {

		if (ms == null) {
			throw new NullPointerException("motive-state must not be null");
		}

		if (b == null) {
			throw new NullPointerException("belief-state must not be null");
		}

		if (out == null) {
			throw new NullPointerException("mot-structure-access must not be null");
		}

		Collection<MotStrcEntry> entries = new HashSet<>();
		Collection<MotiveCoupling<L, F>> couplings;

		for (Desire d : ms.getDesires()) {

			// use only not satisfied desires
			if (!b.isSatisfied(d)) {
				couplings = ms.getCouplings(d);
				boolean isFst = true;
				double mu = 0;

				for (MotiveCoupling<L, F> mc : couplings) {

					// use only activated motive couplings
					if (b.verify(mc.getStatement())) {
						double w = ms.getWeight(mc.getMotive().getLevel());
						double cs = mc.getCouplingStrength();

						if (!isFst) {
							// aggregate partial results
							mu = aggregation.aggregate(mu, (w * cs));
						} else {
							// a first value of mu is needed
							mu = (w * cs);
							isFst = false;
						}
					}
				}

				if (!isFst) {
					LOGGER.info("set motivational-value {} for desire <{}>", format(mu), d);
					entries.add(new MotStrcEntry(d, mu));
				}
			}
		}

		out.putEntries(entries);
	}

}
