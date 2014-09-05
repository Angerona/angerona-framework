package com.github.angerona.fw.motivation.operators;

import net.sf.tweety.logics.fol.syntax.FolFormula;

import com.github.angerona.fw.motivation.Maslow;
import com.github.angerona.fw.motivation.functional.WeightAdjustment;
import com.github.angerona.fw.motivation.functional.DesireSelection;
import com.github.angerona.fw.motivation.functional.StructureAdjustment;
import com.github.angerona.fw.motivation.functional.impl.QuantileDesireSelection;
import com.github.angerona.fw.motivation.functional.impl.StructureAdjustmentImpl;
import com.github.angerona.fw.motivation.functional.impl.WeightAdjustmentImpl;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class MotivationOperator extends GenMotOperator<Maslow, FolFormula> {

	public MotivationOperator() {
		super(new WeightAdjustmentImpl(), new StructureAdjustmentImpl(), new QuantileDesireSelection<FolFormula>());
	}

	private MotivationOperator(WeightAdjustment<Maslow, FolFormula> weightAdjustment, StructureAdjustment<Maslow, FolFormula> structureAdjustment,
			DesireSelection<FolFormula> selection) {
		super(weightAdjustment, structureAdjustment, selection);
	}

	@Override
	protected GenMotOperatorParameter<Maslow, FolFormula> getEmptyParameter() {
		return new MotOperatorParameter();
	}

	@Override
	protected GenMotOperator<Maslow, FolFormula> clone() {
		return new MotivationOperator(this.weightAdjustment.copy(), this.structureAdjustment.copy(), this.selection.copy());
	}

}
