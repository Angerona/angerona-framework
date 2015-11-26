package com.github.kreaturesfw.core.legacy;

/**
 * Base interface for atomic Angerona constructs like: perceptions, actions and
 * plan elements everything what is an input for an violation check.
 * 
 * @author Tim Janus
 */
public interface AngeronaAtom {
	boolean equals(Object other);

	int hashCode();
}
