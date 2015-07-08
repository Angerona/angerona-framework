package com.github.angerona.fw.motivation.data;

import java.util.EnumSet;
import java.util.Set;

/**
 * {@link MaslowIterable} helps to iterate over the set of levels of Maslow's hierarchy of needs.
 * @see also {@link Maslow}
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
