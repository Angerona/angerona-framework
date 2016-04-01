package com.github.kreatures.core;

/**
 * interface for perceptions.
 * @author Tim Janus
 */
public interface Perception extends KReaturesAtom {
	/** @return the id (unique name) of the receiver of the id */
	String getReceiverId();
}
