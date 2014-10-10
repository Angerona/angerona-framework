package com.github.angerona.fw.motivation.operators;

import com.github.angerona.fw.motivation.Maslow;
import com.github.angerona.fw.motivation.functional.DesireSelection;
import com.github.angerona.fw.motivation.functional.MotivationAdjustment;
import com.github.angerona.fw.motivation.functional.WeightAdjustment;
import com.github.angerona.fw.motivation.functional.impl.DesireSelectionImpl;
import com.github.angerona.fw.motivation.functional.impl.MotivationAdjustmentImpl;
import com.github.angerona.fw.motivation.functional.impl.WeightAdjustmentImpl;

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
			DesireSelection selection) {
		super(weightAdjustment, motivationAdjustment, selection);
	}

	@Override
	protected GenMotOperatorParameter<Maslow> getEmptyParameter() {
		return new MotOperatorParameter();
	}

	@Override
	protected GenMotOperator<Maslow> clone() {
		return new MotivationOperator(this.weightAdjustment.copy(), this.motivationAdjustment.copy(), this.selection.copy());
	}

}
