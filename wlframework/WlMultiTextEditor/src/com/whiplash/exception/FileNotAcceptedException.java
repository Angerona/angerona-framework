package com.whiplash.exception;

/** 
 * This exception is thrown when a document controller is ordered
 * to handle a file but does not accept it. 
 * @author Matthias Thimm
 */
public class FileNotAcceptedException extends RuntimeException {

	/** For serialization.  */
	private static final long serialVersionUID = 1L;

	/** Creates a new exception. */
	public FileNotAcceptedException(){
		super("The given file is not accepted by this document controller.");
	}
	
}
