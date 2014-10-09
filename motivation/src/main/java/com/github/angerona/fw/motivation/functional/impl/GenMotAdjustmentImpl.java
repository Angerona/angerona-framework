package com.github.angerona.fw.motivation.functional.impl;

import static com.github.angerona.fw.motivation.utils.FormatUtils.format;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.angerona.fw.Desire;
import com.github.angerona.fw.motivation.MotiveLevel;
import com.github.angerona.fw.motivation.dao.BeliefState;
import com.github.angerona.fw.motivation.dao.MotStructureDao;
import com.github.angerona.fw.motivation.dao.MotiveState;
import com.github.angerona.fw.motivation.functional.AggregationFunction;
import com.github.angerona.fw.motivation.functional.MotivationAdjustment;
import com.github.angerona.fw.motivation.model.MotiveCoupling;

/**
 * {@link GenMotAdjustmentImpl} is the generic implementation of the MotivationAdjustment-function lambda specified in the article
 *
 * @author Manuel Barbi
 * 
 * @param <L>
 */
public abstract class GenMotAdjustmentImpl<L extends MotiveLevel> implements MotivationAdjustment<L> {

	private static final Logger LOGGER = LoggerFactory.getLogger(GenMotAdjustmentImpl.class);

	protected AggregationFunction aggregation;

	public GenMotAdjustmentImpl(AggregationFunction aggregation) {
		if (aggregation == null) {
			throw new NullPointerException("aggregation-function must not be null");
		}

		this.aggregation = aggregation;
	}

	@Override
	public void adjust(MotiveState<L> ms, BeliefState b, MotStructureDao out) {

		if (ms == null) {
			throw new NullPointerException("motive-state must not be null");
		}

		if (b == null) {
			throw new NullPointerException("belief-state must not be null");
		}

		if (out == null) {
			throw new NullPointerException("mot-structure-access must not be null");
		}

		out.clear();
		Collection<MotiveCoupling<L>> couplings;

		for (Desire d : ms.getDesires()) {

			// use only not satisfied desires
			if (!b.isSatisfied(d)) {
				couplings = ms.getCouplings(d);
				boolean isFst = true;
				double mu = 0;

				for (MotiveCoupling<L> mc : couplings) {

					// use only activated motive couplings
					if (mc.getStatement() == null || b.verify(mc.getStatement())) {
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
					out.putEntry(d, mu);
				}
			}
		}
	}

}
