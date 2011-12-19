package com.explodingpixels.widgets;

/**
 * An interface for providers of text modifiers.
 * 
 * @author Matthias Thimm
 */
public interface TextModifierProvider {

	/**
	 * @return "true" if some text should be bold.
	 */
	public boolean isBold();
}
