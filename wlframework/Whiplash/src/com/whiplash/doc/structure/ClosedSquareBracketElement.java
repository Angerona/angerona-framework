package com.whiplash.doc.structure;

/**
 * This element represents a single "]" in a latex document.
 * @author Matthias Thimm
 */
public class ClosedSquareBracketElement extends SingleCharacterElement {

	/** Creates a new closed square bracket element for the given document.
	 * @param model some latex document model.
	 */
	public ClosedSquareBracketElement(LatexDocumentModel model) {
		super(model,']');
	}
}
