package com.github.kreatures.core.error;

/**
 * Base class for own Exception hierarchy.
 * @author Tim Janus
 */
public class KReaturesException extends Exception {
	/**
	 * kill warning
	 */
	private static final long serialVersionUID = -4617370326423073881L;

	public KReaturesException(String message) {
		super(message);
	}
	
	public KReaturesException(String message, Exception inner) {
		super(message, inner);
	}
}
