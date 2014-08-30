package com.github.angerona.fw.motivation;

import java.util.Collection;

import net.sf.tweety.Formula;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.angerona.fw.Desire;
import com.github.angerona.fw.am.secrecy.operators.BaseGenerateOptionsOperator;
import com.github.angerona.fw.am.secrecy.operators.parameter.GenerateOptionsParameter;
import com.github.angerona.fw.logic.Desires;
import com.github.angerona.fw.motivation.functional.WeightAdjustment;
import com.github.angerona.fw.motivation.functional.DesireSelection;
import com.github.angerona.fw.motivation.functional.StructureAdjustment;

/**
 * 
 * @author Manuel Barbi
 * 
 * @param <L>
 * @param <F>
 */
public abstract class GenMotOperator<L extends MotiveLevel, F extends Formula> extends BaseGenerateOptionsOperator {

	private static final Logger LOG = LoggerFactory.getLogger(GenMotOperator.class);

	protected WeightAdjustment<L, F> weightAdjustment;
	protected StructureAdjustment<L, F> structureAdjustment;
	protected DesireSelection<F> selection;

	public GenMotOperator(WeightAdjustment<L, F> weightAdjustment, StructureAdjustment<L, F> structureAdjustment, DesireSelection<F> selection) {
		if (weightAdjustment == null) {
			throw new NullPointerException("delta-calculator must not be null");
		}

		if (structureAdjustment == null) {
			throw new NullPointerException("lambda-calculator must not be null");
		}

		if (selection == null) {
			throw new NullPointerException("desire-selection must not be null");
		}

		this.weightAdjustment = weightAdjustment;
		this.structureAdjustment = structureAdjustment;
		this.selection = selection;

		LOG.debug("created {}", this.getClass().getSimpleName());
	}

	@Override
	protected Integer processImpl(GenerateOptionsParameter preprocessedParameters) {
		@SuppressWarnings("unchecked")
		GenMotOperatorParameter<L, F> param = (GenMotOperatorParameter<L, F>) preprocessedParameters;

		// adjust weights
		weightAdjustment.adjust(param.getMotiveState(), param.getBeliefState(), param.getMotiveState());

		// adjust structure
		structureAdjustment.adjust(param.getMotiveState(), param.getBeliefState(), param.getStructure());

		// select desires
		Collection<Desire> selected = selection.select(param.getBeliefState(), param.getStructure());

		// clear and add all selected desires
		Desires desires = param.getDesires();
		desires.clear();

		for (Desire d : selected) {
			desires.add(d);
		}

		// number of selected desires
		return selected.size();
	}

	@Override
	protected abstract GenMotOperatorParameter<L, F> getEmptyParameter();

	@Override
	protected Integer defaultReturnValue() {
		return 0;
	}

	@Override
	protected abstract GenMotOperator<L, F> clone();
}
