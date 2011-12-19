package com.whiplash.doc.structure;

/**
 * This element represents a single "[" in a latex document.
 * @author Matthias Thimm
 */
public class OpenSquareBracketElement extends SingleCharacterElement {

	/** Creates a new open square bracket element for the given document.
	 * @param model some latex document model.
	 */
	public OpenSquareBracketElement(LatexDocumentModel model) {
		super(model,'[');
	}
}
