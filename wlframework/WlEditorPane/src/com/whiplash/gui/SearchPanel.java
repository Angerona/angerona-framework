package com.whiplash.gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import com.whiplash.gui.laf.*;
import com.whiplash.res.*;
import com.whiplash.util.*;

/**
 * Instances of this class represent panels for searching the content of the document.
 * @author Matthias Thimm
 */
public class SearchPanel extends JPanel implements ActionListener, KeyListener {

	/** For serialization. */
	private static final long serialVersionUID = 1L;

	/** The search handler of this panel. */
	private SearchHandler searchHandler;
	/** The search field. */
	private JTextField searchField;
	/** A label indicating that the current search phrase has not been found. */
	private JLabel phraseNotFoundLabel;
	/** A label indicating that the current search has to be continued from top. */
	private JLabel startedFromTopLabel;
	/** A label indicating that the current search has to be continued from bottom. */
	private JLabel startedFromBottomLabel;
	/** Whether the search should be case-sensitive*/
	private JCheckBox matchCase;
	
	/** This string stores the last search made via key typing.*/
	private String lastSearch = "";
		
	/** Creates a new search panel.
	 * @param searchHandler the search handler of this panel.
	 * @param parent a mouse listener for the search field.
	 */
	public SearchPanel(SearchHandler searchHandler, MouseListener parent){
		//init layout
		super(new FlowLayout(FlowLayout.LEFT,1,2));
		this.setBackground(WlLookAndFeel.COLOR_BACKGROUND_FILLER);
		this.searchHandler = searchHandler;
		// check resource manager
		if(!WlResourceManager.hasDefaultResourceManager())			
			throw new RuntimeException("No default resource manager set.");
		WlResourceManager resourceManager = WlResourceManager.getDefaultResourceManager();
		JButton closeButton = WlLookAndFeel.createBorderlessWlButton("Close", resourceManager.getIcon(WlIcon.CLOSE_ACTIVE, WlIconSize.SIZE_16x16), resourceManager.getIcon(WlIcon.CLOSE_INACTIVE, WlIconSize.SIZE_16x16), this, EditorPaneActionCommands.ACTION_CLOSE_SEARCH_PANEL);
		this.add(closeButton);
		this.add(new Box.Filler(new Dimension(10,1), new Dimension(10,1), new Dimension(10,1)));
		this.searchField = new JTextField();
		if(OsTools.isMacOS()){
			this.searchField.putClientProperty("JTextField.variant", "search");
			this.searchField.putClientProperty( "JTextField.Search.Prompt", resourceManager.getLocalizedText(WlText.INFO_TYPE_TO_SEARCH));
		}
		this.searchField.addMouseListener(parent);
		this.searchField.setMinimumSize(new Dimension(200,this.searchField.getPreferredSize().height));
		this.searchField.setPreferredSize(new Dimension(200,this.searchField.getPreferredSize().height));
		this.searchField.addKeyListener(this);
		this.add(this.searchField);
		this.add(new Box.Filler(new Dimension(10,1), new Dimension(10,1), new Dimension(10,1)));
		JButton previousButton = WlLookAndFeel.createBorderlessWlButton(resourceManager.getLocalizedText(WlText.PREVIOUS), resourceManager.getIcon(WlIcon.PREVIOUS_ACTIVE, WlIconSize.SIZE_16x16), resourceManager.getIcon(WlIcon.PREVIOUS_INACTIVE, WlIconSize.SIZE_16x16), this, EditorPaneActionCommands.ACTION_PREVIOUS_SEARCH_PANEL);
		JButton nextButton = WlLookAndFeel.createBorderlessWlButton(resourceManager.getLocalizedText(WlText.NEXT), resourceManager.getIcon(WlIcon.NEXT_ACTIVE, WlIconSize.SIZE_16x16),  resourceManager.getIcon(WlIcon.NEXT_INACTIVE, WlIconSize.SIZE_16x16), this, EditorPaneActionCommands.ACTION_NEXT_SEARCH_PANEL);
		this.add(previousButton);
		this.add(new Box.Filler(new Dimension(5,1), new Dimension(5,1), new Dimension(5,1)));
		this.add(nextButton);
		this.matchCase = new JCheckBox(resourceManager.getLocalizedText(WlText.MATCHCASE));
		this.add(new Box.Filler(new Dimension(10,1), new Dimension(10,1), new Dimension(10,1)));
		this.add(this.matchCase);
		this.phraseNotFoundLabel = new JLabel(resourceManager.getLocalizedText(WlText.INFO_PHRASE_NOT_FOUND));
		this.phraseNotFoundLabel.setVisible(false);
		this.startedFromBottomLabel = new JLabel(resourceManager.getLocalizedText(WlText.INFO_REACHED_END_OF_DOCUMENT_CONTINUED_BOTTOM));
		this.startedFromBottomLabel.setVisible(false);
		this.startedFromTopLabel = new JLabel(resourceManager.getLocalizedText(WlText.INFO_REACHED_END_OF_DOCUMENT_CONTINUED_TOP));
		this.startedFromTopLabel.setVisible(false);
		this.add(new Box.Filler(new Dimension(10,1), new Dimension(10,1), new Dimension(10,1)));
		this.add(this.phraseNotFoundLabel);
		this.add(this.startedFromBottomLabel);
		this.add(this.startedFromTopLabel);
	}

	/** Returns the current search text.
	 * @return the current search text.
	 */
	protected String getText(){
		return this.searchField.getText();
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#setVisible(boolean)
	 */
	public void setVisible(boolean value){
		super.setVisible(value);
		if(value) this.searchField.requestFocus();			
		else this.searchHandler.regainFocus();		
	}
	
	/** Initiates a search in the search handler.
	 * @param direction the direction of the search.
	 */
	protected void doSearch(int direction){
		int result = this.searchHandler.searchText(this.searchField.getText(), this.matchCase.isSelected(), direction);
		switch(result){
			case SearchHandler.PHRASE_FOUND:
				this.phraseNotFoundLabel.setVisible(false);
				this.startedFromBottomLabel.setVisible(false);
				this.startedFromTopLabel.setVisible(false);
				break;
			case SearchHandler.PHRASE_NOT_FOUND:
				this.phraseNotFoundLabel.setVisible(true);
				this.startedFromBottomLabel.setVisible(false);
				this.startedFromTopLabel.setVisible(false);
				break;
			case SearchHandler.CONTINUED_FROM_BOTTOM:
				this.phraseNotFoundLabel.setVisible(false);
				this.startedFromBottomLabel.setVisible(true);
				this.startedFromTopLabel.setVisible(false);
				break;
			case SearchHandler.CONTINUED_FROM_TOP:
				this.phraseNotFoundLabel.setVisible(false);
				this.startedFromBottomLabel.setVisible(false);
				this.startedFromTopLabel.setVisible(true);
				break;
		} 
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals(EditorPaneActionCommands.ACTION_CLOSE_SEARCH_PANEL))
			this.setVisible(false);
		else if(e.getActionCommand().equals(EditorPaneActionCommands.ACTION_NEXT_SEARCH_PANEL))
			this.doSearch(SearchHandler.FORWARD_SEARCH);
		else if(e.getActionCommand().equals(EditorPaneActionCommands.ACTION_PREVIOUS_SEARCH_PANEL))
			this.doSearch(SearchHandler.BACKWARD_SEARCH);		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyTyped(KeyEvent e) { }

	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyChar() == KeyEvent.VK_ESCAPE && e.getSource() == this.searchField)
			this.setVisible(false);
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
	 */
	@Override
	public synchronized void keyReleased(KeyEvent e) {
		if(Character.isDefined(e.getKeyChar())){
			// the following check is necessary as a quick typer might type a new key before the previous
			// one has been processed. In that case the second key will be processed together
			// with the first one and there is no need to process the second again.
			if(!this.searchField.getText().equals(this.lastSearch) || e.getKeyChar() == KeyEvent.VK_ENTER){
				this.lastSearch = this.searchField.getText();
				this.doSearch(SearchHandler.FORWARD_SEARCH);
			}
		}
	}
}
