package com.whiplash.doc.structure;

/**
 * This element represents a single "^" in a latex document.
 * @author Matthias Thimm
 */
public class SuperscriptElement extends SingleCharacterElement {

	/** Creates a new superscript element for the given document.
	 * @param model some latex document model.
	 */
	public SuperscriptElement(LatexDocumentModel model) {
		super(model,'^');
	}
}
