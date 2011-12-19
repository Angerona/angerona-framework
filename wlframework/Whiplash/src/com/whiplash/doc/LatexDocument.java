package com.whiplash.doc;

import java.awt.*;
import java.io.*;
import java.nio.charset.*;
import javax.swing.event.*;
import javax.swing.text.*;

import com.whiplash.control.*;
import com.whiplash.doc.structure.*;

/**
 * This class represents a Latex document. 
 * @author Matthias Thimm
 * 
 * TODO: don't forget informing undoableeditlisteners and document listeners on changes.
 */
public class LatexDocument extends TextFileDocument {

	/** For serialization.  */
	public static final long serialVersionUID = 1L;

	/** The handler (a separate thread) of the document model. */
	private LatexDocumentModelHandler modelHandler;
	
	/** The actual (text) document used for representing the text. */
	private volatile StyledDocument document;
		
	/** Creates a new empty latex document that will be
	 * encoded with the given encoding.
	 * @param encoding some encoding
	 * @param documentController some document controller. 
	 * @throws IOException this should not happen. */
	public LatexDocument(Charset encoding, WlDocumentController<LatexDocument> documentController) throws IOException {
		super(encoding, documentController);		
	}
	
	/** Creates a new latex document for the given file that 
	 * is interpreted using the given encoding.
	 * @param file a file.
	 * @param encoding some encoding
	 * @param documentController some document controller.
	 * @throws IOException if there was some exception in accessing the file.
	 */
	public LatexDocument(File file, Charset encoding, WlDocumentController<LatexDocument> documentController) throws IOException {
		super(file,encoding,documentController);
	}

	/* (non-Javadoc)
	 * @see com.whiplash.doc.TextFileDocument#init()
	 */
	public void init(){
		this.document = new DefaultStyledDocument();
		this.modelHandler = new LatexDocumentModelHandler(new LatexDocumentModel(this));
		this.modelHandler.start();
	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.doc.TextFileDocument#handleClosed()
	 */
	@Override
	public void handleClosed(){
		super.handleClosed();
		// send model handler the kill signal.
		this.modelHandler.mail(null);
	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.doc.WlDocument#getEditorKit(boolean)
	 */
	public EditorKit getEditorKit(boolean linewrap){
		return new LatexEditorKit(linewrap);
	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.doc.WlDocument#doRemove(int, int)
	 */
	@Override
	protected void doRemove(int offs, int len) throws BadLocationException {
		this.document.remove(offs, len);
		super.doRemove(offs, len);
		this.modelHandler.mail(new LatexRemoveMessage(offs,len));
	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.doc.TextFileDocument#doInsertString(int, java.lang.String, javax.swing.text.AttributeSet)
	 */
	@Override
	protected void doInsertString(int offset, String str, AttributeSet a) throws BadLocationException {
		this.document.insertString(offset, str, a);		
		this.modelHandler.mail(new LatexInsertMessage(offset,str));
	}

	/* (non-Javadoc)
	 * @see javax.swing.text.StyledDocument#addDocumentListener(javax.swing.event.DocumentListener)
	 */
	@Override
	public void addDocumentListener(DocumentListener listener){
		super.addDocumentListener(listener);
		this.document.addDocumentListener(listener);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.text.StyledDocument#removeDocumentListener(javax.swing.event.DocumentListener)
	 */
	@Override
	public void removeDocumentListener(DocumentListener listener){
		super.removeDocumentListener(listener);
		this.document.removeDocumentListener(listener);		
	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.doc.TextFileDocument#addUndoableEditListener(javax.swing.event.UndoableEditListener)
	 */
	@Override
	public void addUndoableEditListener(UndoableEditListener arg0) {
		this.document.addUndoableEditListener(arg0);
	}

	/* (non-Javadoc)
	 * @see com.whiplash.doc.TextFileDocument#createPosition(int)
	 */
	@Override
	public Position createPosition(int arg0) throws BadLocationException {
		return this.document.createPosition(arg0);
	}

	/* (non-Javadoc)
	 * @see com.whiplash.doc.TextFileDocument#getDefaultRootElement()
	 */
	@Override
	public Element getDefaultRootElement() {
		return this.document.getDefaultRootElement();
	}

	/* (non-Javadoc)
	 * @see com.whiplash.doc.TextFileDocument#getEndPosition()
	 */
	@Override
	public Position getEndPosition() {
		return this.document.getEndPosition();
	}

	/* (non-Javadoc)
	 * @see com.whiplash.doc.TextFileDocument#getLength()
	 */
	@Override
	public int getLength() {		
		return this.document.getLength();
	}

	/* (non-Javadoc)
	 * @see com.whiplash.doc.TextFileDocument#getProperty(java.lang.Object)
	 */
	@Override
	public Object getProperty(Object arg0) {
		return this.document.getProperty(arg0);
	}

	/* (non-Javadoc)
	 * @see com.whiplash.doc.TextFileDocument#getRootElements()
	 */
	@Override
	public Element[] getRootElements() {
		return this.document.getRootElements();
	}

	/* (non-Javadoc)
	 * @see com.whiplash.doc.TextFileDocument#getStartPosition()
	 */
	@Override
	public Position getStartPosition() {
		return this.document.getStartPosition();
	}

	/* (non-Javadoc)
	 * @see com.whiplash.doc.TextFileDocument#getText(int, int)
	 */
	@Override
	public String getText(int offset, int length) throws BadLocationException {
		return this.document.getText(offset, length);
	}

	/* (non-Javadoc)
	 * @see com.whiplash.doc.TextFileDocument#getText(int, int, javax.swing.text.Segment)
	 */
	@Override
	public void getText(int offset, int length, Segment txt) throws BadLocationException {
		this.document.getText(offset, length, txt);
	}

	/* (non-Javadoc)
	 * @see com.whiplash.doc.TextFileDocument#putProperty(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void putProperty(Object arg0, Object arg1) {
		this.document.putProperty(arg0, arg1);
	}

	/* (non-Javadoc)
	 * @see com.whiplash.doc.TextFileDocument#removeUndoableEditListener(javax.swing.event.UndoableEditListener)
	 */
	@Override
	public void removeUndoableEditListener(UndoableEditListener arg0) {
		this.document.removeUndoableEditListener(arg0);		
	}

	/* (non-Javadoc)
	 * @see com.whiplash.doc.TextFileDocument#render(java.lang.Runnable)
	 */
	@Override
	public void render(Runnable arg0) {
		this.document.render(arg0);
	}

	/* (non-Javadoc)
	 * @see com.whiplash.doc.TextFileDocument#addStyle(java.lang.String, javax.swing.text.Style)
	 */
	@Override
	public Style addStyle(String arg0, Style arg1) {
		return this.document.addStyle(arg0, arg1);
	}

	/* (non-Javadoc)
	 * @see com.whiplash.doc.TextFileDocument#getBackground(javax.swing.text.AttributeSet)
	 */
	@Override
	public Color getBackground(AttributeSet arg0) {
		return this.document.getBackground(arg0);
	}

	/* (non-Javadoc)
	 * @see com.whiplash.doc.TextFileDocument#getForeground(javax.swing.text.AttributeSet)
	 */
	@Override
	public Color getForeground(AttributeSet arg0) {
		return this.document.getForeground(arg0);
	}

	/* (non-Javadoc)
	 * @see com.whiplash.doc.TextFileDocument#getLogicalStyle(int)
	 */
	@Override
	public Style getLogicalStyle(int arg0) {
		return this.document.getLogicalStyle(arg0);		
	}

	/* (non-Javadoc)
	 * @see com.whiplash.doc.TextFileDocument#getParagraphElement(int)
	 */
	@Override
	public Element getParagraphElement(int arg0) {		
		return this.document.getParagraphElement(arg0);
	}

	/* (non-Javadoc)
	 * @see com.whiplash.doc.TextFileDocument#getStyle(java.lang.String)
	 */
	@Override
	public Style getStyle(String arg0) {
		return this.document.getStyle(arg0);		
	}

	/* (non-Javadoc)
	 * @see com.whiplash.doc.TextFileDocument#removeStyle(java.lang.String)
	 */
	@Override
	public void removeStyle(String arg0) {
		this.document.removeStyle(arg0);		
	}

	/* (non-Javadoc)
	 * @see com.whiplash.doc.TextFileDocument#setCharacterAttributes(int, int, javax.swing.text.AttributeSet, boolean)
	 */
	@Override
	public void setCharacterAttributes(int arg0, int arg1, AttributeSet arg2, boolean arg3) {
		this.document.setCharacterAttributes(arg0, arg1, arg2, arg3);
	}

	/* (non-Javadoc)
	 * @see com.whiplash.doc.TextFileDocument#setLogicalStyle(int, javax.swing.text.Style)
	 */
	@Override
	public void setLogicalStyle(int arg0, Style arg1) {
		this.document.setLogicalStyle(arg0, arg1);		
	}

	/* (non-Javadoc)
	 * @see com.whiplash.doc.TextFileDocument#setParagraphAttributes(int, int, javax.swing.text.AttributeSet, boolean)
	 */
	@Override
	public void setParagraphAttributes(int arg0, int arg1, AttributeSet arg2, boolean arg3) {
		this.document.setParagraphAttributes(arg0, arg1, arg2, arg3);		
	}

	/* (non-Javadoc)
	 * @see com.whiplash.doc.TextFileDocument#getCharacterElement(int)
	 */
	@Override
	public Element getCharacterElement(int arg0) {
		return this.document.getCharacterElement(arg0);
	}

	/* (non-Javadoc)
	 * @see com.whiplash.doc.TextFileDocument#getFont(javax.swing.text.AttributeSet)
	 */
	@Override
	public Font getFont(AttributeSet arg0) {
		return this.document.getFont(arg0);
	}
	
}
