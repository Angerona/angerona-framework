package com.whiplash.gui;

import javax.swing.*;

/**
 * This class enhances JTextPane in several aspects.
 * @author Matthias Thimm
 */
public class WlTextPane extends JTextPane {

	/** For serialization. */
	private static final long serialVersionUID = 1L;

	/** Whether line wrap is enabled. */
	private boolean lineWrap = true;
	
	/* (non-Javadoc)
	 * @see javax.swing.JEditorPane#getScrollableTracksViewportWidth()
	 */
	public boolean getScrollableTracksViewportWidth(){
		return this.lineWrap;
	}
	
	/** Sets the value for the line wrap setting.
	 * @param value some boolean.
	 */
	protected void setLineWrap(boolean value){
		this.lineWrap = value;
	}
	
	/** Returns the value for the line wrap setting.
	 * @return the value for the line wrap setting.
	 */
	protected boolean getLineWrap(){
		return this.lineWrap;
	}
}
