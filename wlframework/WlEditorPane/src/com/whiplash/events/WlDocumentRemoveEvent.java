package com.whiplash.events;

import com.whiplash.doc.*;

/**
 * This event occurs when text is removed from a document.<br> 
 * NOTE: Although a "DocumentEvent" occurs when removing text, the
 * "DocumentEvent" does not contain any information on what was removed.
 * In order to update the number of lines in the editor pane this information is needed
 * and provided by this event.
 * @author Matthias Thimm
 */
public class WlDocumentRemoveEvent {

	/** The string removed. */
	private String removedString;
	/** The document where the removal occurred. */
	private WlDocument document;
	 
	/** Creates a new event.
	 * @param doc some document
	 * @param removedString some string.
	 */
	public WlDocumentRemoveEvent(WlDocument document, String removedString){
		this.document = document;
		this.removedString = removedString;
	}

	/**
	 * @return the removedString
	 */
	public String getRemovedString() {
		return this.removedString;
	}

	/**
	 * @return the document.
	 */
	public WlDocument getDocument() {
		return this.document;
	}	
}
