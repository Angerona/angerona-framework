package com.github.angerona.fw.motivation.operators;

import java.util.Collection;

import com.github.angerona.fw.Desire;
import com.github.angerona.fw.am.secrecy.operators.BaseGenerateOptionsOperator;
import com.github.angerona.fw.am.secrecy.operators.parameter.GenerateOptionsParameter;
import com.github.angerona.fw.logic.Desires;
import com.github.angerona.fw.motivation.MotiveLevel;
import com.github.angerona.fw.motivation.functional.DesireSelection;
import com.github.angerona.fw.motivation.functional.MotivationAdjustment;
import com.github.angerona.fw.motivation.functional.WeightAdjustment;

/**
 * 
 * @author Manuel Barbi
 * 
 * @param <L>
 */
public abstract class GenMotOperator<L extends MotiveLevel> extends BaseGenerateOptionsOperator {

	protected WeightAdjustment<L> weightAdjustment;
	protected MotivationAdjustment<L> motivationAdjustment;
	protected DesireSelection desireSelection;

	public GenMotOperator(WeightAdjustment<L> weightAdjustment, MotivationAdjustment<L> motivationAdjustment, DesireSelection desireSelection) {

		if (weightAdjustment == null) {
			throw new NullPointerException("weight-adjustment must not be null");
		}

		if (motivationAdjustment == null) {
			throw new NullPointerException("motivation-adjustment must not be null");
		}

		if (desireSelection == null) {
			throw new NullPointerException("desire-selection must not be null");
		}

		this.weightAdjustment = weightAdjustment;
		this.motivationAdjustment = motivationAdjustment;
		this.desireSelection = desireSelection;
	}

	@Override
	protected Integer processImpl(GenerateOptionsParameter preprocessedParameters) {
		@SuppressWarnings("unchecked")
		GenMotOperatorParameter<L> param = (GenMotOperatorParameter<L>) preprocessedParameters;

		// adjust weights
		weightAdjustment.adjust(param.getMotiveState(), param.getBeliefState(), param.getMotiveState());

		// adjust motivation
		motivationAdjustment.adjust(param.getMotiveState(), param.getBeliefState(), param.getStructure());

		// select desires
		Collection<Desire> selected = desireSelection.select(param.getBeliefState(), param.getStructure());

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
	protected abstract GenMotOperatorParameter<L> getEmptyParameter();

	@Override
	protected abstract GenMotOperator<L> clone();
}
