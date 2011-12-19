package com.whiplash.gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;
import javax.swing.undo.*;

import com.whiplash.config.*;
import com.whiplash.doc.*;
import com.whiplash.events.*;
import com.whiplash.gui.laf.*;

/**
 * This class provides an editor pane (consisting of an JTextPane with line numbers)
 * and syntax coloring and syntax check functionalities.
 * @author Matthias Thimm
 * @param <T> the actual document class.
 */
public class WlEditorPane<T extends WlDocument> extends WlComponent implements AdjustmentListener, WlDocumentListener, SearchHandler, ConfigurationListener {
	
    /** For serialization.  */
	private static final long serialVersionUID = 1L;

	/** The actual text pane. */
	private WlTextPane textPane;
	/** A text pane for the line numbers. */
	private WlLineNumbersPane lineNumbersArea;
	/** The main scroll pane */
	private JScrollPane scrollPane;
	/** The (invisible scroll pane) of the line numbers area. */
	private JScrollPane scrollPaneLines;
	/** The search panel. */
	private SearchPanel searchPanel;
	/** The undo manager of the document. */
	private UndoManager undoManager;
		
	/** Place holder left of the lower scroll bar */
	private Box.Filler filler;
	
	/** The document of this pane.  */
	private T document;

	/** Creates a new editor pane for the given document.
	 * @param document the document represented by this pane.
	 */
	public WlEditorPane(T document) {
		super();
		this.setMinimumSize(new Dimension(160,180));
		this.setPreferredSize(new Dimension(400,500));
		this.document = document;		
		this.document.addDocumentListener(this);
		this.undoManager = new UndoManager();
		this.document.addUndoableEditListener(this.undoManager);
		// register as listener to some configuration options
		WlEditorPaneConfigurationOptions.CONF_DISPLAY_FONT.addConfigurationListener(this);
		WlEditorPaneConfigurationOptions.CONF_LINE_WRAP.addConfigurationListener(this);
		// init gui components
		this.setLayout(new BorderLayout());
		JPanel tempPanel = new JPanel(new BorderLayout());
		tempPanel.setBorder(BorderFactory.createLineBorder(WlLookAndFeel.DEFAULT_THINLINE_COLOR, 1));
		this.add(tempPanel,BorderLayout.CENTER);
		// init text pane
		this.textPane = new WlTextPane();		
		this.textPane.setFont(WlEditorPaneConfigurationOptions.CONF_DISPLAY_FONT.getFont());
		EditorKit editorKit = this.document.getEditorKit(WlEditorPaneConfigurationOptions.CONF_LINE_WRAP.getValue());
		if(editorKit != null){
			this.textPane.setEditorKitForContentType(editorKit.getContentType(), editorKit);
			this.textPane.setContentType(editorKit.getContentType());			
		}
		this.textPane.setLineWrap(WlEditorPaneConfigurationOptions.CONF_LINE_WRAP.getValue());
		this.textPane.setDocument(document);
		this.textPane.setCaret(new WlEditorCaret());
		this.textPane.setMargin(new Insets(4,4,4,4));		 
		this.textPane.addMouseListener(this);
		// init line numbers area (override to disable line wrap)
		this.lineNumbersArea = new WlLineNumbersPane(this, this.textPane);
		this.lineNumbersArea.setFont(WlEditorPaneConfigurationOptions.CONF_DISPLAY_FONT.getFont());
		this.lineNumbersArea.setLineWrap(WlEditorPaneConfigurationOptions.CONF_LINE_WRAP.getValue());
		this.lineNumbersArea.addMouseListener(this);		
		// do layout		
		this.scrollPane = new JScrollPane(this.textPane);
		this.scrollPane.setViewportView(this.textPane);
		this.scrollPane.setBorder(BorderFactory.createEmptyBorder());
		this.scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		this.scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		this.scrollPane.getVerticalScrollBar().addAdjustmentListener(this);
		JPanel tempPanel2 = new JPanel(new BorderLayout());
		this.filler = new Box.Filler(new Dimension(1,15), new Dimension(1,15), new Dimension(1,15));
		tempPanel2.add(this.filler,BorderLayout.SOUTH);
		tempPanel2.setBackground(WlLookAndFeel.COLOR_BACKGROUND_FILLER);
		this.scrollPaneLines = new JScrollPane(this.lineNumbersArea,ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		this.scrollPaneLines.setViewportView(this.lineNumbersArea);
		this.scrollPaneLines.setBorder(BorderFactory.createEmptyBorder());		
		tempPanel2.add(this.scrollPaneLines,BorderLayout.CENTER);
		// init search panel
		this.searchPanel = new SearchPanel(this,this);
		this.searchPanel.setVisible(false);
		//add components to view					
		tempPanel.add(scrollPane, BorderLayout.CENTER);
		tempPanel.add(tempPanel2, BorderLayout.WEST);
		tempPanel.add(searchPanel,BorderLayout.SOUTH);
		this.scrollPane.addMouseListener(this);
		this.scrollPane.getHorizontalScrollBar().addMouseListener(this);
		this.scrollPane.getVerticalScrollBar().addMouseListener(this);		
	}
	
	/** Returns the document of this editor pane.
	 * @return the document of this editor pane.
	 */
	public T getDocument(){
		return this.document;
	}
	
	/** Returns the current search text.
	 * @return the current search text.
	 */
	public String getSearchText(){
		return this.searchPanel.getText();
	}
	
	/** Sets the document of this editor pane.
	 * @param document a document.
	 */
	@SuppressWarnings("unchecked")
	public void setDocument(WlDocument document){
		this.cleanDocument();
		this.document = (T) document;
		this.document.addDocumentListener(this);
		this.undoManager = new UndoManager();
		this.document.addUndoableEditListener(this.undoManager);
		EditorKit editorKit = this.document.getEditorKit(WlEditorPaneConfigurationOptions.CONF_LINE_WRAP.getValue());
		if(editorKit != null){
			this.textPane.setEditorKitForContentType(editorKit.getContentType(), editorKit);
			this.textPane.setContentType(editorKit.getContentType());			
		}
		this.textPane.setDocument(document);
		this.document.fireTitleChangedEvent();
	}
	
	/** Removes diverse listeners from the current document.
	 * Invoke this method when this editor pane is about to lose
	 * the document. */
	private void cleanDocument(){
		this.document.removeDocumentListener(this);
		this.document.removeUndoableEditListener(this.undoManager);
	}
	
	/** Returns the undo manager of this pane.
	 * @return the undo manager of this pane.
	 */
	public UndoManager getUndoManager(){
		return this.undoManager;
	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.gui.WlComponent#focusGained()
	 */
	@Override
	protected void focusGained() {	
		super.focusGained();
		this.textPane.requestFocus();		
	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.gui.WlComponent#getTitle()
	 */
	@Override
	public String getTitle() {		
		if(this.document == null)
			return "";
		return this.document.getTitle();
	}

	/* (non-Javadoc)
	 * @see com.whiplash.gui.WlComponent#componentClosed()
	 */
	@Override
	protected void componentClosed(){
		this.cleanDocument();
		WlEditorPaneConfigurationOptions.CONF_DISPLAY_FONT.removeConfigurationListener(this);
		WlEditorPaneConfigurationOptions.CONF_LINE_WRAP.removeConfigurationListener(this);
	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.gui.WlComponent#paint(java.awt.Graphics)
	 */
	public void paint(Graphics g){		
		super.paint(g);
		this.filler.setPreferredSize(new Dimension(1,this.scrollPane.getHorizontalScrollBar().getHeight()));		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.AdjustmentListener#adjustmentValueChanged(java.awt.event.AdjustmentEvent)
	 */
	@Override
	public void adjustmentValueChanged(AdjustmentEvent e) {
		// propagate any change of the scroll bar to the line numbers area.
		if( this.scrollPaneLines.getVerticalScrollBar() != null)
			this.scrollPaneLines.getVerticalScrollBar().setValue(this.scrollPane.getVerticalScrollBar().getValue());
		this.validate();
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.event.DocumentListener#insertUpdate(javax.swing.event.DocumentEvent)
	 */
	@Override
	public void insertUpdate(DocumentEvent e) {
		this.lineNumbersArea.showLineNumbers();		
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.DocumentEvent)
	 */
	@Override
	public void removeUpdate(DocumentEvent e) {	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.events.WlDocumentListener#removeUpdate(com.whiplash.events.WlDocumentRemoveEvent)
	 */
	@Override
	public void removeUpdate(WlDocumentRemoveEvent e) {		
		this.lineNumbersArea.showLineNumbers();
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.DocumentListener#changedUpdate(javax.swing.event.DocumentEvent)
	 */
	@Override
	public void changedUpdate(DocumentEvent e) { }

	/** Opens the find panel. */
	public void find(){
		this.searchPanel.setVisible(true);
	}
	
	/** Finds the next occurence of the given search phrase.*/
	public void findNext(){
		this.searchPanel.doSearch(SearchHandler.FORWARD_SEARCH);
	}

	/* (non-Javadoc)
	 * @see com.whiplash.gui.SearchHandler#regainFocus()
	 */
	@Override
	public void regainFocus() {
		this.focusGained();		
	}

	/* (non-Javadoc)
	 * @see com.whiplash.gui.SearchHandler#searchText(java.lang.String, boolean, int)
	 */
	@Override
	public int searchText(String searchText, boolean matchCase, int direction) {
		this.refrestActionEnablement();
		if(searchText.equals("")){
			this.textPane.select(0, 0);
			return SearchHandler.PHRASE_FOUND;
		}
		int startedAgain = 0;
		int idx = -1;
		int pos = (this.textPane.getSelectionStart() != this.textPane.getSelectionEnd()) ? this.textPane.getSelectionStart() : this.textPane.getCaretPosition();
		try {			
			if(this.textPane.getSelectedText() != null && this.textPane.getSelectedText().toLowerCase().equals(searchText.toLowerCase())) pos++;
			if(direction == SearchHandler.FORWARD_SEARCH){
				String text = this.document.getText(pos,this.document.getLength()-pos);
				if(matchCase) idx = text.indexOf(searchText);
				else idx = text.toLowerCase().indexOf(searchText.toLowerCase());				
				if(idx == -1){
					// try to start again from top	
					if(matchCase) idx = this.document.getText(0,this.document.getLength()).indexOf(searchText);
					else idx = this.document.getText(0,this.document.getLength()).toLowerCase().indexOf(searchText.toLowerCase());
					if(idx != -1)
						startedAgain = SearchHandler.CONTINUED_FROM_TOP;
				}
			}else{
				String text = this.document.getText(0,pos);
				if(matchCase) idx = text.lastIndexOf(searchText);
				else idx = text.toLowerCase().lastIndexOf(searchText.toLowerCase());
				if(idx == -1){
					// try to start again from bottom	
					if(matchCase) idx = this.document.getText(0,this.document.getLength()).lastIndexOf(searchText);
					else idx = this.document.getText(0,this.document.getLength()).toLowerCase().lastIndexOf(searchText.toLowerCase());
					if(idx != -1)
						startedAgain = SearchHandler.CONTINUED_FROM_BOTTOM;
				}
			}
		} catch (BadLocationException e) {
			// this should not happen
			return SearchHandler.PHRASE_NOT_FOUND;
		}
		if(idx == -1){
			this.textPane.select(0, 0);
			return SearchHandler.PHRASE_NOT_FOUND;
		}		
		if(startedAgain != 0){
			this.textPane.select(idx, idx+searchText.length());
			return startedAgain;
		}
		if(direction == SearchHandler.BACKWARD_SEARCH)
			this.textPane.select(idx, idx+searchText.length());
		else this.textPane.select(pos+idx, pos+idx+searchText.length());
		return SearchHandler.PHRASE_FOUND;
	}

	/** Changes the font size of this editor pane.
	 * @param increase whether to increase the font size by one (otherwise it is
	 *  decreased).
	 */
	public void changeFontSize(boolean increase){
		int currentFontsize = this.textPane.getFont().getSize();
		int idx = -1;
		for(int i = 0; i < FontConfigurationOption.FONT_SIZES.length; i++){
			if(FontConfigurationOption.FONT_SIZES[i].equals(currentFontsize)){
				idx = i;
				break;
			}
		}
		if(increase) idx++;
		else idx--;
		if(FontConfigurationOption.FONT_SIZES.length > idx && idx >= 0){
			this.textPane.setFont(this.textPane.getFont().deriveFont(new Float(FontConfigurationOption.FONT_SIZES[idx])));
			this.lineNumbersArea.setFont(this.lineNumbersArea.getFont().deriveFont(new Float(FontConfigurationOption.FONT_SIZES[idx])));
		}
	}	
	
	/** Toggles the line wrap setting for this editor pane. */
	public void toggleLineWrap(){
		boolean newValue = !this.textPane.getLineWrap();
		EditorKit editorKit = this.document.getEditorKit(newValue);
		if(editorKit != null){
			this.textPane.setEditorKitForContentType(editorKit.getContentType(), editorKit);
			this.textPane.setContentType(editorKit.getContentType());			
		}
		this.textPane.setDocument(this.document);
		this.textPane.setLineWrap(newValue);
		this.lineNumbersArea.setLineWrap(newValue);
		this.textPane.validate();
	}
	
	/** Returns the line wrap setting of this editor pane.
	 * @return "true" if this editor pane has line wrap enabled.
	 */
	public boolean getLineWrap(){
		return this.textPane.getLineWrap();
	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.events.ConfigurationListener#configurationChanged(com.whiplash.events.ConfigurationEvent)
	 */
	@Override
	public void configurationChanged(ConfigurationEvent evt) {
		if(evt.getConfigurationOption() == WlEditorPaneConfigurationOptions.CONF_DISPLAY_FONT){
			this.textPane.setFont(((FontConfigurationOption)evt.getConfigurationOption()).getFont());
			this.lineNumbersArea.setFont(((FontConfigurationOption)evt.getConfigurationOption()).getFont());
		}else if(evt.getConfigurationOption() == WlEditorPaneConfigurationOptions.CONF_LINE_WRAP){
			EditorKit editorKit = this.document.getEditorKit(WlEditorPaneConfigurationOptions.CONF_LINE_WRAP.getValue());
			if(editorKit != null){
				this.textPane.setEditorKitForContentType(editorKit.getContentType(), editorKit);
				this.textPane.setContentType(editorKit.getContentType());			
			}
			this.textPane.setDocument(this.document);
			this.textPane.setLineWrap(((BooleanConfigurationOption)evt.getConfigurationOption()).getValue());
			this.lineNumbersArea.setLineWrap(((BooleanConfigurationOption)evt.getConfigurationOption()).getValue());
			this.textPane.validate();			
		}		
	}

}
