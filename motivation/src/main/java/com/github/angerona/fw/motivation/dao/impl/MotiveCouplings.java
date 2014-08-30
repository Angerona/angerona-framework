package com.github.angerona.fw.motivation.dao.impl;

import java.util.Set;

import net.sf.tweety.logics.fol.syntax.FolFormula;

import com.github.angerona.fw.motivation.Maslow;
import com.github.angerona.fw.motivation.model.MotiveCoupling;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class MotiveCouplings extends GenMotiveCouplings<Maslow, FolFormula> {

	public MotiveCouplings() {}

	public MotiveCouplings(Set<MotiveCoupling<Maslow, FolFormula>> couplings) {
		super(couplings);
	}

	@Override
	protected GenMotiveCouplings<Maslow, FolFormula> create() {
		return new MotiveCouplings();
	}

}
