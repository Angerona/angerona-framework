package com.whiplash.control;

import java.awt.event.*;
import java.io.*;

import javax.swing.*;

import com.explodingpixels.macwidgets.*;
import com.explodingpixels.widgets.*;
import com.whiplash.gui.laf.*;
import com.whiplash.res.*;

/**
 * Instances of this class represent file nodes in the file tree of the file tree explorer.
 * @author Matthias Thimm
 */
public class WlFileTreeNode extends SourceListItem implements TextModifierProvider, ButtonProvider {

	/** The file of this node. */
	private File file;
	/** Whether the file of this node is opened. */
	private boolean isOpened;
	/** Whether the file of this node is stained. */
	private boolean isStained;
	
	/** The close button of this tree node. */
	private JButton button;	
	/** some action listener for the button. */
	private ActionListener actionListener;
	
	/** Property for opened status changed. */
	private static final String PROPERTY_OPENED_CHANGED = "OPENED";
	/** Property for tree node. */
	protected static final String PROPERTY_TREENODE = "TREENODE";
	
	/** Creates a new file node.
	 * @param file some file.
	 * @param isOpened whether the file of this node is opened.
	 * @param isStained whether the file of this node is stained.
	 * @param actionListener some action listener.
	 */
	public WlFileTreeNode(File file, boolean isOpened, boolean isStained, ActionListener actionListener){
		super(file.getName());
		this.file = file;
		this.isOpened = isOpened;	
		this.isStained = isStained;
		this.actionListener = actionListener;
		this.initButton();
	}
	
	/** Inits the close button. */
	private void initButton(){
		// check resource manager
		if(!WlResourceManager.hasDefaultResourceManager())			
			throw new RuntimeException("No default resource manager set.");
		WlResourceManager resourceManager = WlResourceManager.getDefaultResourceManager();		
		if(this.isOpened)
			this.button = WlLookAndFeel.createBorderlessWlButton(resourceManager.getLocalizedText(WlText.CLOSE), resourceManager.getIcon(WlIcon.CLOSE_ACTIVE, WlIconSize.SIZE_16x16), resourceManager.getIcon(WlIcon.CLOSE_ACTIVE, WlIconSize.SIZE_16x16), this.actionListener, "");
		else
			this.button = WlLookAndFeel.createBorderlessWlButton(resourceManager.getLocalizedText(WlText.CLOSE), resourceManager.getIcon(WlIcon.CLOSE_INACTIVE, WlIconSize.SIZE_16x16), resourceManager.getIcon(WlIcon.CLOSE_INACTIVE, WlIconSize.SIZE_16x16), this.actionListener, "");
		this.button.putClientProperty(WlFileTreeNode.PROPERTY_TREENODE, this);
	}

	/**
	 * @return the file
	 */
	public File getFile() {
		return this.file;
	}

	/**
	 * @param file the file to set
	 */
	public void setFile(File file) {
		this.file = file;		
		super.setText(this.toString());		
	}

	/**
	 * @return the isOpened
	 */
	public boolean isOpened() {
		return this.isOpened;
	}

	/**
	 * @param isOpened the isOpened to set
	 */
	protected void setOpened(boolean isOpened) {
		boolean oldIsOpen = this.isOpened;
		this.isOpened = isOpened;
		if(oldIsOpen != isOpened){
			this.initButton();			
			this.fSupport.firePropertyChange(WlFileTreeNode.PROPERTY_OPENED_CHANGED, oldIsOpen, isOpened);
		}
	}
	
	/**
	 * @param isStained the isStained to set
	 */
	protected void setStained(boolean isStained) {
		this.isStained = isStained;		
		super.setText(this.toString());		
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){		
		String prefix = "";
		if(this.isStained) prefix = "*";
		return prefix + this.file.getName();
	}

	/* (non-Javadoc)
	 * @see com.explodingpixels.widgets.TextModifierProvider#isBold()
	 */
	@Override
	public boolean isBold() {	
		return this.isOpened;
	}

	/* (non-Javadoc)
	 * @see com.explodingpixels.widgets.ButtonProvider#getButton()
	 */
	@Override
	public JButton getButton() {
		return this.button;
	}

	/* (non-Javadoc)
	 * @see com.explodingpixels.widgets.ButtonProvider#getPreferredButtonWidth()
	 */
	public int getPreferredButtonWidth(){
		return 16;
	}
}
