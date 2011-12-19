package com.whiplash.doc.structure;

/**
 * This element represents a single "}" in a latex document.
 * @author Matthias Thimm
 */
public class ClosedCurlyBracketElement extends SingleCharacterElement {

	/** Creates a new closed curly bracket element for the given document.
	 * @param model some latex document model.
	 */
	public ClosedCurlyBracketElement(LatexDocumentModel model) {
		super(model,'}');
	}
}
