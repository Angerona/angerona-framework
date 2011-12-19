package com.whiplash.event;

/**
 * Classes implementing this interface react on changes in document controllers.
 * @author Matthias Thimm
 */
public interface WlDocumentControllerListener {
	
	/** Informs the listener that a new document has been opened.
	 * @param e some event.
	 */
	public void documentOpened(DocumentControllerEvent e);
	
	/** Informs the listener that a document has been saved.
	 * @param e some event.
	 */
	public void documentSaved(DocumentControllerEvent e);
	
	/** Informs the listener that the stained status of a document has been changed.
	 * @param e some event.
	 */
	public void documentStainedStatusChanged(DocumentControllerEvent e);
	
	/** Informs the listener that a document has been closed.
	 * @param e some event.
	 */
	public void documentClosed(DocumentControllerEvent e);
	
	/** Informs the listener that a new document has been created.
	 * @param e some event.
	 */
	public void documentCreated(DocumentControllerEvent e);
}
