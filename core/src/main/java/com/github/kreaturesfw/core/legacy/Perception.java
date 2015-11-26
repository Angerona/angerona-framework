package com.github.kreaturesfw.core.legacy;

/**
 * interface for perceptions.
 * @author Tim Janus
 */
public interface Perception extends AngeronaAtom {
	/** @return the id (unique name) of the receiver of the id */
	String getReceiverId();
}