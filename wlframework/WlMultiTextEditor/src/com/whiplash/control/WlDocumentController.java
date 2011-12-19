package com.whiplash.control;

import java.awt.*;
import java.io.*;
import java.nio.charset.*;
import java.util.*;

import com.whiplash.doc.*;
import com.whiplash.event.*;
import com.whiplash.exception.*;
import com.whiplash.gui.*;
import com.whiplash.res.*;

/**
 * This class controls the documents and files for the multi-text editor.
 * @author Matthias Thimm
 */
public class WlDocumentController<T extends TextFileDocument> {

	/** The documents currently opened. */
	private java.util.List<T> documents;
	/** The document factory used to create new documents. */
	private DocumentFactory<T> documentFactory;
	/** The extension of files this document controller accepts. */
	private String extension;
	/**The listeners of this object. */
	private java.util.List<WlDocumentControllerListener> listener;
	
	/**
	 * Creates a new document controller with no documents.
	 * @param documentFactory a document factory for creating documents.
	 * @param extension the extension of files this document controller accepts.
	 */
	public WlDocumentController(DocumentFactory<T> documentFactory, String extension){
		this.documents = new LinkedList<T>();
		this.documentFactory = documentFactory;
		this.extension = extension;
		this.listener = new LinkedList<WlDocumentControllerListener>();
	}
	
	/** Adds the given listener to this controller's listeners.
	 * @param listener a listener
	 * @return "true" whether the addition was successful
	 */
	public boolean addDocumentControllerListener(WlDocumentControllerListener listener){
		return this.listener.add(listener);
	}
	
	/** Removes the given listener from this controller's listeners.
	 * @param listener a listener.
	 * @return "true" if the removal of successful.
	 */
	public boolean removeDocumentControllerListener(WlDocumentControllerListener listener){
		return this.listener.remove(listener);
	}
		
	/**
	 * Checks whether this document controller accepts the given file.
	 * @param file a file.
	 * @return "true" iff this document controller accepts the given file.
	 */
	public boolean accepts(File file){
		if(file instanceof NullFile) return true;
		return (file.isFile() || !file.exists()) && file.getName().endsWith(this.extension);
	}
	
	/** Returns the document for the given file (or null if there
	 * is no corresponding document).
	 * @param file some file.
	 * @return the document for the file or null.
	 */
	public T getDocumentForFile(File file){
		for(T doc: this.documents)
			if(doc.getFile() != null && doc.getFile().equals(file))
				return doc;
		return null;
	}
	
	/** Returns the extension of files this controller handles.
	 * @return the extension of files this controller handles.
	 */
	public String getExtension(){
		return this.extension;
	}
	
	/** Creates a new document with the given encoding.
	 * @param encoding an encoding.
	 * @return the document created.
	 */
	public T newDocument(Charset encoding){
		T document = this.documentFactory.createDocument(encoding, this);
		this.documents.add(document);
		this.fireDocumentCreatedEvent(new DocumentControllerEvent(document,this,DocumentControllerEvent.DOCUMENT_CREATED));
		return document;		
	}
	
	/** Opens a new document from the given file using the given encoding.
	 * If the file could not be opened, the caller of this method is 
	 * ordered to give a corresponding info message to the user.
	 * @param file a file.
	 * @param encoding an encoding.
	 * @param parentComponent the parent component relative to which the dialog will be shown.
	 * @return a text document or "null" if opening of the file failed.
	 * @throws FileNotAcceptedException if the given file is not accepted.
	 */
	public TextFileDocument openDocument(File file, Charset encoding, Component parentComponent) throws FileNotAcceptedException{
		if(!this.accepts(file))
			throw new FileNotAcceptedException();
		// first check whether the given document is already openend
		for(TextFileDocument document: this.documents)
			if(document.getFile() != null && document.getFile().equals(file))
				return document;
		try {
			T document = this.documentFactory.createDocument(file, encoding, this);
			this.documents.add(document);
			this.fireDocumentOpenedEvent(new DocumentControllerEvent(document,this,DocumentControllerEvent.DOCUMENT_OPENED));
			return document;
		} catch (IOException e) {
			// check resource manager
			if(!WlResourceManager.hasDefaultResourceManager())			
				throw new RuntimeException("No default resource manager set.");
			WlResourceManager resourceManager = WlResourceManager.getDefaultResourceManager();
			WlDialog.showErrorMessage(resourceManager.getLocalizedText(WlText.DIALOG_FILECOULDNOTBEOPENED) + e.getMessage(), parentComponent);
			return null;
		}		
	}
	
	/** Checks whether the given document can be closed. If the file has unsaved changes the calling gui controller
	 * is ordered to show the user a confirmation dialog whether the document should be saved or not.
	 * Note, that the document is not removed from this controller.
	 * @param document a document.
	 * @param parentComponent the parent component relative to which the dialog will be shown.
	 * @return "true" iff the document has been closed.
	 * @throws DocumentNotKnownException if the given document is not known by this controller.
	 */
	public boolean mayCloseDocument(TextFileDocument document, WlComponent parentComponent) throws DocumentNotKnownException{
		if(!this.documents.contains(document))
			throw new DocumentNotKnownException(document,this);
		if(document.isStained()){
			int result = WlDialog.showUnsavedChangesDialog(parentComponent);
			if(result == WlDialog.CANCEL_ACTION)
				return false;
			if(result == WlDialog.SAVE_ACTION)
				if(!this.saveDocument(document, parentComponent))
					return false;
		}
		return true;
	}
	
	/** Removes all documents. */
	public void clear(){
		for(TextFileDocument document: this.documents)
			this.removeDocument(document);
	}
	
	/** Removes the given document. The document will just be removed from
	 * this controller without checking the stained status.
	 * @param document a document
	 */
	public void removeDocument(TextFileDocument document){
		this.removeDocument(document,true);		
	}
	
	/** Removes the given document. The document will just be removed from
	 * this controller without checking the stained status.
	 * @param document a document
	 * @param generateEvent whether to generate the event of removing a document.
	 */
	public void removeDocument(TextFileDocument document, boolean generateEvent){
		this.documents.remove(document);
		if(generateEvent)
			this.fireDocumentClosedEvent(new DocumentControllerEvent(document,this,DocumentControllerEvent.DOCUMENT_CLOSED));
		// inform document that it has been closed
		//System.out.println("X");
		document.handleClosed();
	}
	
	/** Saves the given document. If the document  has no associated file yet the user is asked to 
	 * provide one.
	 * @param document a document.
	 * @param parentComponent the parent component relative to which the dialog will be shown.
	 * @return "true" if the document was saved successfully.
	 * @throws DocumentNotKnownException if the given document is not known by this controller.
	 */
	public boolean saveDocument(TextFileDocument document, WlComponent parentComponent) throws DocumentNotKnownException{
		if(!this.documents.contains(document))
			throw new DocumentNotKnownException(document,this);
		if(document.getFile() instanceof NullFile){
			File file = WlDialog.showSaveFileDialog(parentComponent, this.extension);
			if(file == null)
				return false;
			return this.saveDocument(document, file, parentComponent);
		}
		try {
			document.saveToFile();
		} catch (IOException e) {
			// check resource manager
			if(!WlResourceManager.hasDefaultResourceManager())			
				throw new RuntimeException("No default resource manager set.");
			WlResourceManager resourceManager = WlResourceManager.getDefaultResourceManager();
			WlDialog.showErrorMessage(resourceManager.getLocalizedText(WlText.DIALOG_FILECOULDNOTBESAVED) + e.getMessage(), parentComponent);
			return false;
		}
		this.fireDocumentSavedEvent(new DocumentControllerEvent(document,this,DocumentControllerEvent.DOCUMENT_SAVED));
		return true;
	}

	/** Saves the given document to the given file.
	 * @param document a document.
	 * @param file a file.
	 * @param parentComponent the parent component relative to which the dialog will be shown.
	 * @return "true" if the document was saved successfully.
	 * @throws DocumentNotKnownException if the given document is not known by this controller.
	 * @throws FileNotAcceptedException if the given file is not accepted by this controller.
	 */
	public boolean saveDocument(TextFileDocument document, File file, WlComponent parentComponent) throws DocumentNotKnownException, FileNotAcceptedException{
		if(!this.accepts(file))
			throw new FileNotAcceptedException();
		if(!this.documents.contains(document))
			throw new DocumentNotKnownException(document,this);
		try {
			document.saveToFile(file);
		} catch (IOException e) {
			// check resource manager
			if(!WlResourceManager.hasDefaultResourceManager())			
				throw new RuntimeException("No default resource manager set.");
			WlResourceManager resourceManager = WlResourceManager.getDefaultResourceManager();
			WlDialog.showErrorMessage(resourceManager.getLocalizedText(WlText.DIALOG_FILECOULDNOTBESAVED) + e.getMessage(), parentComponent);
			return false;
		}
		this.fireDocumentSavedEvent(new DocumentControllerEvent(document,this,DocumentControllerEvent.DOCUMENT_SAVED));
		return true;
	}
	
	/** Checks whether this document controller handles the given document.
	 * @param document a document.
	 * @return "true" iff this controller handles the given document.
	 */
	public boolean isHandling(WlDocument document){
		return this.documents.contains(document);
	}
	
	/** Checks whether this document controller handles a document for the given file.
	 * @param File file some file.
	 * @return "true" iff this controller handles a document for the given file.
	 */
	public boolean isHandling(File file){
		for(T doc: this.documents)
			if(doc.getFile().equals(file))
				return true;
		return false;
	}
	
	/** Informs this controller that the given document changed its title.
	 * @param doc some document.
	 */
	public void stainedStatusChanged(TextFileDocument doc){
		if(this.isHandling(doc))
			this.fireDocumentStainedStatusChangedEvent(new DocumentControllerEvent(doc,this,DocumentControllerEvent.DOCUMENT_CHANGED_TITLE));
	}
	/** Informs this controller that the given document changed its title.
	 * @param doc some document.
	 * @param oldFile the former file of the document.
	 */	
	public void fileSaved(TextFileDocument doc, File oldFile){
		if(this.isHandling(doc))
			this.fireDocumentSavedEvent(new DocumentControllerEvent(doc,this,oldFile,DocumentControllerEvent.DOCUMENT_SAVED));
	}
	
	/** Informs the listener of this object that a new document has been opened.
	 * @param e some event.
	 */
	private void fireDocumentOpenedEvent(DocumentControllerEvent e){
		for(WlDocumentControllerListener listener: this.listener)
			listener.documentOpened(e);
	}

	/** Informs the listener of this object that a document has been saved.
	 * @param e some event.
	 */
	private void fireDocumentSavedEvent(DocumentControllerEvent e){
		for(WlDocumentControllerListener listener: this.listener)
			listener.documentSaved(e);
	}
	
	/** Informs the listener of this object that a document has changed its title.
	 * @param e some event.
	 */
	private void fireDocumentStainedStatusChangedEvent(DocumentControllerEvent e){
		for(WlDocumentControllerListener listener: this.listener)
			listener.documentStainedStatusChanged(e);
	}
	
	/** Informs the listener of this object that a document has been closed.
	 * @param e some event.
	 */
	private void fireDocumentClosedEvent(DocumentControllerEvent e){
		for(WlDocumentControllerListener listener: this.listener)
			listener.documentClosed(e);
	}
	
	/** Informs the listener of this object that a new document has been created.
	 * @param e some event.
	 */
	private void fireDocumentCreatedEvent(DocumentControllerEvent e){
		for(WlDocumentControllerListener listener: this.listener)
			listener.documentCreated(e);
	}
}
