package com.github.kreaturesfw.core.basic;

import com.github.kreaturesfw.core.legacy.AngeronaAtom;

/**
 * interface for perceptions.
 * @author Tim Janus
 */
public interface Perception extends AngeronaAtom {
	/** @return the id (unique name) of the receiver of the id */
	String getReceiverId();
}
