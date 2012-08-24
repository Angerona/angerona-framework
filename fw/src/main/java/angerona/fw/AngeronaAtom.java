package angerona.fw;

import angerona.fw.logic.ViolatesResult;

/**
 * Base interface for atomar Angerona constructs like:
 * perceptions, actions and plan elements everything whats 
 * an input for an violation check.
 * @author Tim Janus
 */
public interface AngeronaAtom {
	boolean equals(Object other);
	
	/** returns the violates result which was calculated by the last violates operator call */
	ViolatesResult violates();
}
