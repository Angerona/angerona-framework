package com.github.angerona.fw.motivation;

import java.util.Iterator;
import java.util.Set;

/**
 * 
 * @author Manuel Barbi
 * 
 * @param <L>
 */
public abstract class GenLevelIterable<L extends MotiveLevel> implements Iterable<L> {

	@Override
	public Iterator<L> iterator() {
		return getLevels().iterator();
	}

	protected abstract Set<L> getLevels();

	public abstract GenLevelIterable<L> copy();

}
