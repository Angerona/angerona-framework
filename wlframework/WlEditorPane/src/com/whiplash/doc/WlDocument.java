package com.whiplash.doc;

import java.awt.*;
import java.util.*;

import javax.swing.event.*;
import javax.swing.text.*;

import com.whiplash.events.*;
import com.whiplash.gui.*;

/**
 * This class is the model class for an editor pane.
 * @author Matthias Thimm
 */
public abstract class WlDocument implements StyledDocument {

	/** For serialization. */
	public static final long serialVersionUID = 1L;
	
	/** Listener for receiving text remove events. */
	private java.util.List<DocumentListener> documentListener;
	
	/** Initializes a new document. */
	public WlDocument(){
		this.documentListener = new LinkedList<DocumentListener>();
	}
	
	/** Returns the title of this document.
	 * @return the title of this document.
	 */
	public abstract String getTitle();
	
	/** Returns the editor kit associated with this document.
	 * @param linewrap whether to support line wrap.
	 * @return the editor kit associated with this document.
	 */
	public abstract EditorKit getEditorKit(boolean linewrap);
	
	/* (non-Javadoc)
	 * @see javax.swing.text.StyledDocument#addDocumentListener(javax.swing.event.DocumentListener)
	 */
	public void addDocumentListener(DocumentListener listener){
		this.documentListener.add(listener);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.text.StyledDocument#removeDocumentListener(javax.swing.event.DocumentListener)
	 */
	public void removeDocumentListener(DocumentListener listener){
		this.documentListener.remove(listener);
	}	
	
	/** Returns the document listeners of this document.
	 * @return the document listeners of this document.
	 */
	public Collection<? extends DocumentListener> getDocumentListeners(){
		return Collections.unmodifiableList(this.documentListener);
	}
	
	/** Informs the listeners of this object of the given event.
	 * @param e some event.
	 */
	private void fireDocumentRemoveEvent(WlDocumentRemoveEvent e){
		for(DocumentListener listener: this.documentListener)
			if(listener instanceof WlDocumentListener)
				((WlDocumentListener)listener).removeUpdate(e);
	}

	/** Causes all editor panes which are watching this document to re-read its title. */
	public void fireTitleChangedEvent(){
		for(WlEditorPane<?> pane: this.getEditorPanes())
			pane.refreshTitle();
	}
	
	/** Returns the set of editor panes which watch
	 * this document.
	 * @return a collection of editor panes.
	 */
	public Collection<WlEditorPane<?>> getEditorPanes(){
		Set<WlEditorPane<?>> panes = new HashSet<WlEditorPane<?>>();
		for(DocumentListener listener: this.getDocumentListeners())
			if(listener instanceof WlEditorPane<?>)
				panes.add((WlEditorPane<?>)listener);
		return panes;
	}

	/* (non-Javadoc)
	 * @see javax.swing.text.AbstractDocument#remove(int, int)
	 */
	protected abstract void doRemove(int offs, int len) throws BadLocationException;
	
	/* (non-Javadoc)
	 * @see javax.swing.text.AbstractDocument#remove(int, int)
	 */
	public void remove(int offs, int len) throws BadLocationException{
		String removal = this.getText(offs, len);
		this.doRemove(offs, len);
		this.fireDocumentRemoveEvent(new WlDocumentRemoveEvent(this,removal));
	}

	/* (non-Javadoc)
	 * @see javax.swing.text.Document#addUndoableEditListener(javax.swing.event.UndoableEditListener)
	 */
	public abstract void addUndoableEditListener(UndoableEditListener arg0);

	/* (non-Javadoc)
	 * @see javax.swing.text.Document#createPosition(int)
	 */
	public abstract Position createPosition(int arg0) throws BadLocationException;

	/* (non-Javadoc)
	 * @see javax.swing.text.Document#getDefaultRootElement()
	 */
	public abstract Element getDefaultRootElement();
	
	/* (non-Javadoc)
	 * @see javax.swing.text.Document#getEndPosition()
	 */
	public abstract Position getEndPosition();

	/* (non-Javadoc)
	 * @see javax.swing.text.Document#getLength()
	 */
	public abstract int getLength();

	/* (non-Javadoc)
	 * @see javax.swing.text.Document#getProperty(java.lang.Object)
	 */
	public abstract Object getProperty(Object arg0);

	/* (non-Javadoc)
	 * @see javax.swing.text.Document#getRootElements()
	 */
	public abstract Element[] getRootElements();

	/* (non-Javadoc)
	 * @see javax.swing.text.Document#getStartPosition()
	 */
	public abstract Position getStartPosition();

	/* (non-Javadoc)
	 * @see javax.swing.text.Document#getText(int, int)
	 */
	public abstract String getText(int arg0, int arg1) throws BadLocationException;

	/* (non-Javadoc)
	 * @see javax.swing.text.Document#getText(int, int, javax.swing.text.Segment)
	 */
	public abstract void getText(int arg0, int arg1, Segment arg2) throws BadLocationException;

	/* (non-Javadoc)
	 * @see javax.swing.text.Document#insertString(int, java.lang.String, javax.swing.text.AttributeSet)
	 */
	public abstract void insertString(int arg0, String arg1, AttributeSet arg2) throws BadLocationException;
	
	/* (non-Javadoc)
	 * @see javax.swing.text.Document#putProperty(java.lang.Object, java.lang.Object)
	 */
	public abstract void putProperty(Object arg0, Object arg1);
	
	/* (non-Javadoc)
	 * @see javax.swing.text.Document#removeUndoableEditListener(javax.swing.event.UndoableEditListener)
	 */
	public abstract void removeUndoableEditListener(UndoableEditListener arg0);

	/* (non-Javadoc)
	 * @see javax.swing.text.Document#render(java.lang.Runnable)
	 */
	public abstract void render(Runnable arg0);

	/* (non-Javadoc)
	 * @see javax.swing.text.StyledDocument#addStyle(java.lang.String, javax.swing.text.Style)
	 */
	public abstract Style addStyle(String arg0, Style arg1);

	/* (non-Javadoc)
	 * @see javax.swing.text.StyledDocument#getBackground(javax.swing.text.AttributeSet)
	 */
	public abstract Color getBackground(AttributeSet arg0);

	/* (non-Javadoc)
	 * @see javax.swing.text.StyledDocument#getCharacterElement(int)
	 */
	public abstract Element getCharacterElement(int arg0);

	/* (non-Javadoc)
	 * @see javax.swing.text.StyledDocument#getFont(javax.swing.text.AttributeSet)
	 */
	public abstract Font getFont(AttributeSet arg0);

	/* (non-Javadoc)
	 * @see javax.swing.text.StyledDocument#getForeground(javax.swing.text.AttributeSet)
	 */
	@Override
	public abstract Color getForeground(AttributeSet arg0);

	/* (non-Javadoc)
	 * @see javax.swing.text.StyledDocument#getLogicalStyle(int)
	 */
	public abstract Style getLogicalStyle(int arg0);

	/* (non-Javadoc)
	 * @see javax.swing.text.StyledDocument#getParagraphElement(int)
	 */
	public abstract Element getParagraphElement(int arg0);

	/* (non-Javadoc)
	 * @see javax.swing.text.StyledDocument#getStyle(java.lang.String)
	 */
	public abstract Style getStyle(String arg0);

	/* (non-Javadoc)
	 * @see javax.swing.text.StyledDocument#removeStyle(java.lang.String)
	 */
	public abstract void removeStyle(String arg0);

	/* (non-Javadoc)
	 * @see javax.swing.text.StyledDocument#setCharacterAttributes(int, int, javax.swing.text.AttributeSet, boolean)
	 */
	public abstract void setCharacterAttributes(int arg0, int arg1, AttributeSet arg2, boolean arg3);

	/* (non-Javadoc)
	 * @see javax.swing.text.StyledDocument#setLogicalStyle(int, javax.swing.text.Style)
	 */
	public abstract void setLogicalStyle(int arg0, Style arg1);

	/* (non-Javadoc)
	 * @see javax.swing.text.StyledDocument#setParagraphAttributes(int, int, javax.swing.text.AttributeSet, boolean)
	 */
	public abstract void setParagraphAttributes(int arg0, int arg1, AttributeSet arg2, boolean arg3);
}
