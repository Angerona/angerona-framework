package com.github.angerona.fw.motivation;

import java.util.Iterator;
import java.util.Set;

/**
 * {@link GenLevelIterable} is an abstract class to iterate over any set of {@link MotiveLevel}s.
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

	/**
	 * 
	 * @return a certain set of MotiveLevels
	 */
	protected abstract Set<L> getLevels();

	/**
	 * 
	 * @return a replica of this instance
	 */
	public abstract GenLevelIterable<L> copy();

}
