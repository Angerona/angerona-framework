package com.github.angerona.fw.motivation.functional.impl;

import com.github.angerona.fw.motivation.Maslow;
import com.github.angerona.fw.motivation.functional.DeficiencyPartition;

/**
 * partition that separates the motive levels of Maslow's hierarchy of needs into deficiency and non-deficiency levels
 * @see also {@link Maslow}
 * 
 * @author Manuel Barbi
 * 
 */
public class MaslowDeficiency implements DeficiencyPartition<Maslow> {

	public boolean contains(Maslow level) {
		switch (level) {
		case PHYSIOLOGICAL_NEEDS:
			return true;
		case SAFETY_NEEDS:
			return true;
		case LOVE_AND_BELONGING:
			return true;
		case ESTEEM:
			return false;
		case SELF_ACTUALIZATION:
			return false;
		default:
			throw new IllegalArgumentException("unhandled motive-level");
		}
	}

	@Override
	public DeficiencyPartition<Maslow> copy() {
		return new MaslowDeficiency();
	}

}
