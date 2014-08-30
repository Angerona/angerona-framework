package com.github.angerona.fw.motivation.functional.impl;

import net.sf.tweety.logics.fol.syntax.FolFormula;

import com.github.angerona.fw.motivation.Maslow;
import com.github.angerona.fw.motivation.functional.AggregationFunction;
import com.github.angerona.fw.motivation.functional.StructureAdjustment;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class StructureAdjustmentImpl extends GenStrcAdjustmentImpl<Maslow, FolFormula> {

	public StructureAdjustmentImpl() {
		super(new MycinCombination());
	}

	public StructureAdjustmentImpl(AggregationFunction aggregation) {
		super(aggregation);
	}

	@Override
	public StructureAdjustment<Maslow, FolFormula> copy() {
		return new StructureAdjustmentImpl(this.aggregation.copy());
	}

}
