package com.whiplash.doc.structure;

/**
 * This element represents a single "{" in a latex document.
 * @author Matthias Thimm
 */
public class OpenCurlyBracketElement extends SingleCharacterElement {

	/** Creates a new open curly bracket element for the given document.
	 * @param model some latex document model.
	 */
	public OpenCurlyBracketElement(LatexDocumentModel model) {
		super(model,'{');
	}
}
