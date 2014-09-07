package com.github.angerona.fw.motivation.island.operators;

import net.sf.tweety.logics.fol.syntax.FolFormula;

import com.github.angerona.fw.motivation.Maslow;
import com.github.angerona.fw.motivation.functional.ReliabilityChecker;
import com.github.angerona.fw.motivation.functional.impl.ReliabilityCheckerImpl;
import com.github.angerona.fw.motivation.operators.GenMotOperatorParameter;
import com.github.angerona.fw.motivation.operators.MotOperatorParameter;
import com.github.angerona.fw.motivation.operators.MotivationOperator;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class IslandMotOperator extends MotivationOperator {

	@Override
	protected GenMotOperatorParameter<Maslow, FolFormula> getEmptyParameter() {
		return new MotOperatorParameter() {

			@Override
			protected ReliabilityChecker checker() {
				return new ReliabilityCheckerImpl(getAgent());
			}

		};
	}

}
