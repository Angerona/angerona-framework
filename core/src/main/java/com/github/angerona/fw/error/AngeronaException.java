package com.github.angerona.fw.error;

/**
 * Base class for own Exception hierarchy.
 * @author Tim Janus
 */
public class AngeronaException extends Exception {
	/**
	 * kill warning
	 */
	private static final long serialVersionUID = -4617370326423073881L;

	public AngeronaException(String message) {
		super(message);
	}
	
	public AngeronaException(String message, Exception inner) {
		super(message, inner);
	}
}
