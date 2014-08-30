package com.github.angerona.fw.motivation.model;

import com.github.angerona.fw.motivation.MotiveLevel;

/**
 * 
 * @author Manuel Barbi
 * 
 * @param <L>
 */
public class Motive<L extends MotiveLevel> {

	private String identifier;
	private L level;

	public Motive(String identifier, L level) {
		if (identifier == null) {
			throw new NullPointerException("identifier must not be null");
		}

		if (level == null) {
			throw new NullPointerException("motive-level must not be null");
		}

		this.identifier = identifier.toLowerCase();
		this.level = level;
	}

	public String getIdentifier() {
		return identifier;
	}

	public L getLevel() {
		return level;
	}

	@Override
	public String toString() {
		return "[" + identifier + ", " + level + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((identifier == null) ? 0 : identifier.hashCode());
		result = prime * result + ((level == null) ? 0 : level.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Motive<?> other = (Motive<?>) obj;
		if (identifier == null) {
			if (other.identifier != null)
				return false;
		} else if (!identifier.equals(other.identifier))
			return false;
		if (level == null) {
			if (other.level != null)
				return false;
		} else if (!level.equals(other.level))
			return false;
		return true;
	}

}
