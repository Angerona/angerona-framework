package com.github.kreatures.core;

/**
 * Base interface for atomic KReatures constructs like: perceptions, actions and
 * plan elements everything what is an input for an violation check.
 * 
 * @author Tim Janus
 */
public interface KReaturesAtom {
	boolean equals(Object other);

	int hashCode();
}
