package com.whiplash.doc;

import java.awt.*;
import java.io.*;
import java.nio.charset.*;

import javax.swing.event.*;
import javax.swing.text.*;

import com.whiplash.control.*;

/**
 * This class represents a text file document.
 * @author Matthias Thimm
 */
public abstract class TextFileDocument extends WlDocument {

	/** For serialization. */
	public static final long serialVersionUID = 1L;

	/** The file corresponding to this document. */
	private File file;	
	/**	The encoding used for reading and writing to the disk. */
	private Charset encoding;
	/** Whether this document is stained, i.e. whether it has changes that
	 * have not been saved to disk yet. */
	private boolean isStained;
	
	/** The controller of this document. */
	private WlDocumentController<?> documentController;
	
	/** Creates a new empty document that will be
	 * encoded with the given encoding.
	 * @param encoding some encoding
	 * @param documentController some document controller. 
	 * @throws IOException this should not happen. */
	public TextFileDocument(Charset encoding, WlDocumentController<?> documentController) throws IOException{
		this(new NullFile(), encoding, documentController);
	}
	
	/** Creates a new document for the given file that 
	 * is interpreted using the given encoding.
	 * @param file a file.
	 * @param encoding some encoding
	 * @param documentController some document controller.
	 * @throws IOException if there was some exception in accessing the file.
	 */
	public TextFileDocument(File file, Charset encoding, WlDocumentController<?> documentController) throws IOException {
		super();
		this.file = file;
		this.encoding = encoding;		
		this.documentController = documentController;
		this.init();
		this.loadFromFile();		
	}
	
	/** Do some initializations before the file is loaded. */
	public abstract void init();
	
	/** Returns the encoding of this document.
	 * @return the encoding of this document.
	 */
	public Charset getEncoding(){
		return this.encoding;
	}
	
	/** Sets the encoding of this file.
	 * @param encoding some encoding;
	 */
	public void setEncoding(Charset encoding){
		if(!this.encoding.equals(encoding))
			this.setStained(true);
		this.encoding = encoding;
	}
	
	/** Returns the file of this document.
	 * @return the file of this document.
	 */
	public File getFile(){
		return this.file;
	}
	
	/** Loads the content of this document's file into this document. 
	 * @throws IOException if accessing this document's file was not successful.
	 */
	private void loadFromFile() throws IOException {
		if(this.file instanceof NullFile)
			return;
		StringBuilder textContent = new StringBuilder();
		FileInputStream fileReader = new FileInputStream(this.file); 
		InputStreamReader inputReader = new InputStreamReader(fileReader, this.encoding);
		BufferedReader bufferedReader =  new BufferedReader(inputReader);
		String line = null;
		try{
			if((line = bufferedReader.readLine()) != null){
				textContent.append(line);
				while( (line = bufferedReader.readLine()) != null){
					textContent.append(System.getProperty("line.separator"));
					textContent.append(line);				
				}
			}
		}finally{
			bufferedReader.close();
		}		
		try {
			// remove content of the document if not empty
			if(this.getLength() > 0)
				this.remove(0, this.getLength());
			this.insertString(0, textContent.toString(), null);
		} catch (BadLocationException e) {
			// this should not happen
			throw new RuntimeException(e);
		}
		this.setStained(false);
	}
	
	/** Saves the content of this document to file. The file will be overwritten without
	 *  consideration. If the file does not exist it will be created.
	 * @throws IOException if file access was not successful.
	 */
	public void saveToFile() throws IOException{
		this.saveToFile(true);
	}
	
	/** Saves the content of this document to file. The file will be overwritten without
	 *  consideration. If the file does not exist it will be created.
	 *  @param generateEvent whether an event for the save will be generated.
	 * @throws IOException if file access was not successful.
	 */
	private void saveToFile(boolean generateEvent) throws IOException{
		if(this.file instanceof NullFile)
			throw new RuntimeException("Cannot save a null file.");
		// create the file if it does not exist (nothing happens if the file already exists)
		this.file.createNewFile();
		// only perform writing when the content is non-empty
		if(this.getLength() != 0){
			FileOutputStream fileOutput = new FileOutputStream(this.file);
			OutputStreamWriter output = new OutputStreamWriter(fileOutput, this.encoding);
			try {
				output.write(this.getText(0, this.getLength()));
			} catch (BadLocationException e) {
				// this should not happen
				throw new RuntimeException(e);
			}finally{
				output.close();
			}
		}
		this.setStained(false);
		if(generateEvent)
			this.documentController.fileSaved(this, this.file);
	}
	
	/** Saves the content of this document to the given file and associates this
	 * document permanently with the given file. The file will be overwritten without
	 * consideration. If the file does not exist it will be created.
	 * @param file a file.
	 * @throws IOException if file access was not successful.
	 */
	public void saveToFile(File file) throws IOException{
		if(file == null) throw new IllegalArgumentException("File should not be null.");
		if(file instanceof NullFile) throw new IllegalArgumentException("Cannot save a null file."); 
		File oldFile = this.file;
		this.file = file;
		try{
			this.saveToFile(false);
		}catch(IOException e){
			throw e;
		}
		this.documentController.fileSaved(this, oldFile);
		if(!oldFile.equals(file))
			this.fireTitleChangedEvent();
	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.editorpane.WlDocument#getTitle()
	 */
	@Override
	public String getTitle() {		
		String title = "";
		if(this.isStained)
			title = "*";
		if(this.file != null)
			return title + this.file.getName();
		throw new RuntimeException("File should not be null.");
	}
	
	/** Checks whether this document is stained, i.e. whether it has changes that
	 * have not been saved to disk yet.
	 * @return "true" iff this document is stained.
	 */
	public boolean isStained(){
		return this.isStained;
	}
	
	/** Sets the stained state of this document and
	 * in the case of a state change alerts document listeners
	 * that the title of this document has changed.
	 * @param value the value for the stained state.
	 */
	private void setStained(boolean value){
		if(this.isStained == value)
			return;
		this.isStained = value;
		this.fireTitleChangedEvent();
		this.documentController.stainedStatusChanged(this);
	}	

	/* (non-Javadoc)
	 * @see com.whiplash.doc.WlDocument#removeDocumentListener(javax.swing.event.DocumentListener)
	 */
	@Override
	public void removeDocumentListener(DocumentListener listener){
		super.removeDocumentListener(listener);
		// if no editor panes watch this document any more remove it
		if(this.getEditorPanes().isEmpty())
			this.documentController.removeDocument(this);
	}
	
	/** This method is called when the document has been closed in order
	 * to allow for cleanup operations. */
	public void handleClosed(){	}
	
	/* (non-Javadoc)
	 * @see javax.swing.text.AbstractDocument#insertString(int, java.lang.String, javax.swing.text.AttributeSet)
	 */
	@Override
	public void insertString(int offset, String str, AttributeSet a) throws BadLocationException{
		this.doInsertString(offset, str, a);
		if(!str.equals(""))
			this.setStained(true);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.text.AbstractDocument#insertString(int, java.lang.String, javax.swing.text.AttributeSet)
	 */
	protected abstract void doInsertString(int offset, String str, AttributeSet a)  throws BadLocationException;
		
	/* (non-Javadoc)
	 * @see com.whiplash.doc.WlDocument#doRemove(int, int)
	 */
	@Override
	protected void doRemove(int offs, int len) throws BadLocationException {
		if(len != 0)
			this.setStained(true);
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
