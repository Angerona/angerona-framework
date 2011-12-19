package com.explodingpixels.widgets;

import javax.swing.*;

/**
 * An interface for providers of buttons.
 * 
 * @author Matthias Thimm
 */
public interface ButtonProvider {

	
	/** Returns the button:.
	 * @return the component.
	 */
	public JButton getButton();
		
	/** The preferred width of the button.
	 * @return the preferred width of the button.
	 */
	public int getPreferredButtonWidth();
}
