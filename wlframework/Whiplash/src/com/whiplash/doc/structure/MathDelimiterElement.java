package com.whiplash.doc.structure;

/**
 * This element represents a single "$" in a latex document.
 * @author Matthias Thimm
 */
public class MathDelimiterElement extends SingleCharacterElement {

	/** Creates a new math delimiter element for the given document.
	 * @param model some latex document model.
	 */
	public MathDelimiterElement(LatexDocumentModel model) {
		super(model,'$');
	}
}
