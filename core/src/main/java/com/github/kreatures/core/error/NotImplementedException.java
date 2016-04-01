package com.github.kreatures.core.error;

/**
 * Own not implemented exception class. The one defined in
 * sun.reflect.generic.reflectiveObjects generates warnings
 * when building java-doc.
 * 
 * @author Tim Janus
 */
public class NotImplementedException extends Error {

	/** kill warning */
	private static final long serialVersionUID = -4304241177430562403L;

	public NotImplementedException() {
		super("Not implemented yet.");
	}
	
	public NotImplementedException(String message) {
		super(message);
	}

}
