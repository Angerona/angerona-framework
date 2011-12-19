package com.whiplash.event;

import com.whiplash.control.*;
import com.whiplash.doc.*;

/**
 * This event occurs when a document has been opened/saved/closed by a document controller.
 * @author Matthias Thimm
 */
public class DocumentControllerEvent {
	
	/** Constant referring to opening a document. */
	public static final int DOCUMENT_OPENED = 1;
	/** Constant referring to saving a document. */
	public static final int DOCUMENT_SAVED = 2;
	/** Constant referring to closing a document. */
	public static final int DOCUMENT_CLOSED = 3;
	/** Constant referring to the creation of a new document. */
	public static final int DOCUMENT_CREATED = 4;
	/** Constant referring to the creation of a new document. */
	public static final int DOCUMENT_CHANGED_TITLE = 5;
	
	/** The document that has been opened. */
	private TextFileDocument document;
	/** The controller that initiated the event. */
	private WlDocumentController<?> controller;
	/** the type of the event. */
	private int type;
	/** For storing some user data (e.g. old filename)*/
	private Object userData;
	
	/**
	 * Creates a new event for a document controller.
	 * @param document the document that has been opened.
	 * @param controller the controller that initiated the event.
	 * @param one of DOCUMENT_OPENED, DOCUMENT_SAVED, DOCUMENT_CLOSED, DOCUMENT_CREATED, DOCUMENT_CHANGED_TITLE
	 */
	public DocumentControllerEvent(TextFileDocument document, WlDocumentController<?> controller, int type){
		this.document = document;
		this.controller = controller;
		this.type = type;
	}
	
	/**
	 * Creates a new event for a document controller.
	 * @param document the document that has been opened.
	 * @param userData some user data.
	 * @param controller the controller that initiated the event. 
	 * @param one of DOCUMENT_OPENED, DOCUMENT_SAVED, DOCUMENT_CLOSED, DOCUMENT_CREATED, DOCUMENT_CHANGED_TITLE
	 */
	public DocumentControllerEvent(TextFileDocument document, WlDocumentController<?> controller, Object userData, int type){
		this(document,controller,type);
		this.userData = userData;		
	}

	/**
	 * @return the document that has been opened.
	 */
	public TextFileDocument getDocument() {
		return this.document;
	}

	/**
	 * @return the controller that initiated the event.
	 */
	public WlDocumentController<?> getController() {
		return this.controller;
	}	
	
	/**
	 * @return the type of the event.
	 */
	public int getType() {
		return this.type;
	}	
	
	/**
	 * @return the user data.
	 */
	public Object getUserData() {
		return this.userData;
	}
	
}
