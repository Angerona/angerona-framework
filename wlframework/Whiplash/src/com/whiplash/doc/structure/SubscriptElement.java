package com.whiplash.doc.structure;

/**
 * This element represents a single "_" in a latex document.
 * @author Matthias Thimm
 */
public class SubscriptElement extends SingleCharacterElement {

	/** Creates a new subscript element for the given document.
	 * @param model some latex document model.
	 */
	public SubscriptElement(LatexDocumentModel model) {
		super(model,'_');
	}
}
