package com.whiplash.gui;

import javax.swing.text.*;

/** This caret allows that the selection color of selected text in 
 * the text component remains the same even if focus is lost.
 * @author Matthias Thimm
 */
public class WlEditorCaret extends DefaultCaret {

	/** For serialization. */
	private static final long serialVersionUID = 1L;
	/** Whether the text component has focus. */
	private boolean isFocused;

	/* (non-Javadoc)
	 * @see javax.swing.text.DefaultCaret#getSelectionPainter()
	 */
	protected Highlighter.HighlightPainter getSelectionPainter() {
		return DefaultHighlighter.DefaultPainter;
	}

	/* (non-Javadoc)
	 * @see javax.swing.text.DefaultCaret#setSelectionVisible(boolean)
	 */
	public void setSelectionVisible(boolean hasFocus){
		if (hasFocus != isFocused){
			isFocused = hasFocus;
			super.setSelectionVisible(false);
			super.setSelectionVisible(true);
		}
	}
}
