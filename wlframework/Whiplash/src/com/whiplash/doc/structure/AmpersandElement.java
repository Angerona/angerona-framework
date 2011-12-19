package com.whiplash.doc.structure;

/**
 * This element represents a single "&" in a latex document.
 * @author Matthias Thimm
 */
public class AmpersandElement extends SingleCharacterElement {

	/** Creates a new ampersand element for the given document.
	 * @param model some latex document model.
	 */
	public AmpersandElement(LatexDocumentModel model) {
		super(model,'&');
	}
}
