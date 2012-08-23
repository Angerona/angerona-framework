package angerona.fw;

import angerona.fw.logic.ViolatesResult;

/**
 * Base interface for atomar Angerona constructs like:
 * Intentions (Skills), perceptions, actions...
 * @author Tim Janus
 */
public interface AngeronaAtom {
	boolean equals(Object other);
	
	/** returns the violates result which was calculated by the last violates operator call */
	ViolatesResult violates();
}
