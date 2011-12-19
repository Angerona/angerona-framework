package com.whiplash.doc.structure;

/**
 * This element represents a single "#" in a latex document.
 * @author Matthias Thimm
 */
public class HashmarkElement extends SingleCharacterElement {

	/** Creates a new hash mark element for the given document.
	 * @param model some latex document model.
	 */
	public HashmarkElement(LatexDocumentModel model) {
		super(model,'#');
	}
}
