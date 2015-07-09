package com.github.angerona.fw.motivation.operators;

import java.util.Set;

import com.github.angerona.fw.Desire;
import com.github.angerona.fw.am.secrecy.operators.parameter.PlanParameter;
import com.github.angerona.fw.island.operators.AdvancedIntentionUpdateOperator;
import com.github.angerona.fw.motivation.data.MotiveLevel;
import com.github.angerona.fw.motivation.functional.DesireSelection;
import com.github.angerona.fw.motivation.functional.MotivationAdjustment;
import com.github.angerona.fw.motivation.functional.WeightAdjustment;
import com.github.angerona.fw.motivation.operators.parameters.GenMotOperatorParameter;

/**
 * 
 * @author Manuel Barbi
 *
 * @param <L>
 */
public abstract class GenMotOperator<L extends MotiveLevel> extends AdvancedIntentionUpdateOperator {

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
	protected Desire chooseIntention(PlanParameter preParam, Set<Desire> desires, Desire pursued) {
		@SuppressWarnings("unchecked")
		GenMotOperatorParameter<L> param = (GenMotOperatorParameter<L>) preParam;

		// adjust weights
		weightAdjustment.adjust(param.getMotiveState(), param.getBeliefState(), param.getMotiveState());

		// adjust motivation
		motivationAdjustment.adjust(param.getMotiveState(), param.getBeliefState(), param.getStructure());

		// select desire
		return desireSelection.select(param.getBeliefState(), param.getStructure());
	}

	@Override
	protected abstract GenMotOperatorParameter<L> getEmptyParameter();

	@Override
	protected abstract GenMotOperator<L> clone();

}
