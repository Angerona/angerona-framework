package com.github.angerona.fw.motivation;

import java.util.EnumSet;
import java.util.Set;

/**
 * 
 * @author Manuel Barbi
 * 
 */
public class MaslowIterable extends GenLevelIterable<Maslow> {

	private final Set<Maslow> MASLOW_LEVELS = EnumSet.allOf(Maslow.class);

	@Override
	protected Set<Maslow> getLevels() {
		return MASLOW_LEVELS;
	}

	@Override
	public GenLevelIterable<Maslow> copy() {
		return new MaslowIterable();
	}

}
