package com.whiplash.exception;

import com.whiplash.control.*;
import com.whiplash.doc.*;

/** This exception is thrown when a document controller is instructed to
 * perform an operation with some document but the document is not known
 * to the document controller. 
 * @author Matthias Thimm
 */
public class DocumentNotKnownException extends RuntimeException {

	/** For serialization. */
	private static final long serialVersionUID = 1L;

	/** The document. */
	private TextFileDocument document;
	/** The document controller. */
	private WlDocumentController<?> controller;
	
	/** Creates a new exception for the given document and controller.
	 * @param document a text document.
	 * @param controller a document controller.
	 */
	public DocumentNotKnownException(TextFileDocument document, WlDocumentController<?> controller){
		super("The document '" + document.getTitle() + "' is not known to the controller '" + controller.toString() +"'.");
		this.document = document;
		this.controller = controller;				
	}
	
	/** Returns the document.
	 * @return the document.
	 */
	public TextFileDocument getDocument(){
		return this.document;
	}
	
	/** Returns the document controller.
	 * @return the document controller.
	 */
	public WlDocumentController<?> getDocumentController(){
		return this.controller;
	}
	
}