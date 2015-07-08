package com.github.angerona.fw.motivation.operators;

import com.github.angerona.fw.motivation.data.Maslow;
import com.github.angerona.fw.motivation.functional.DesireSelection;
import com.github.angerona.fw.motivation.functional.MotivationAdjustment;
import com.github.angerona.fw.motivation.functional.WeightAdjustment;
import com.github.angerona.fw.motivation.functional.impl.DesireSelectionImpl;
import com.github.angerona.fw.motivation.functional.impl.MotivationAdjustmentImpl;
import com.github.angerona.fw.motivation.functional.impl.WeightAdjustmentImpl;
import com.github.angerona.fw.motivation.operators.parameters.GenMotOperatorParameter;
import com.github.angerona.fw.motivation.operators.parameters.MotOperatorParameter;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class MotivationOperator extends GenMotOperator<Maslow> {

	public MotivationOperator() {
		super(new WeightAdjustmentImpl(), new MotivationAdjustmentImpl(), new DesireSelectionImpl());
	}

	private MotivationOperator(WeightAdjustment<Maslow> weightAdjustment, MotivationAdjustment<Maslow> motivationAdjustment,
			DesireSelection desireSelection) {
		super(weightAdjustment, motivationAdjustment, desireSelection);
	}

	@Override
	protected GenMotOperatorParameter<Maslow> getEmptyParameter() {
		return new MotOperatorParameter();
	}

	@Override
	protected GenMotOperator<Maslow> clone() {
		return new MotivationOperator(this.weightAdjustment.copy(), this.motivationAdjustment.copy(), this.desireSelection.copy());
	}

}
