package com.whiplash.doc.structure;

import javax.swing.text.*;

/**
 * This element represents a single special character in a latex document.
 * @author Matthias Thimm
 */
public abstract class SingleCharacterElement extends AbstractLatexElement {
	
	/** Creates a new single character element for the given document.
	 * @param model some latex document model.
	 * @param parent the parent element.
	 * @param character the single character of this element.
	 */
	public SingleCharacterElement(LatexDocumentModel model, char character) {
		super(model);
		this.characters = new char[1];
		this.characters[0] = character;
	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.doc.structure.AbstractLatexElement#getLength()
	 */
	@Override
	protected int getLength() {
		return 1;
	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.doc.structure.AbstractLatexElement#indexOfCharacter(char)
	 */
	@Override
	protected  int indexOfCharacter(char ch){
		return this.characters[0] == ch ? 0 : -1;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.text.Element#getEndOffset()
	 */
	@Override
	public int getEndOffset(){
		return this.startOffset + 1;
	}

	/* (non-Javadoc)
	 * @see com.whiplash.doc.structure.AbstractLatexElement#getText(int, int)
	 */
	public String getText(int offset, int length){
		String str = "";
		str += this.characters[0];
		return str;
	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.doc.structure.AbstractLatexElement#getText(int, int, javax.swing.text.Segment)
	 */
	public void getText(int offset, int length, Segment txt){
		txt.array = this.characters;
		txt.offset = 0;
		txt.count = 1;			
	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.doc.structure.AbstractLatexElement#getText(int, int, javax.swing.text.Segment, int)
	 */
	public int getText(int offset, int length, Segment txt, int txtOffset){
		txt.array[txtOffset] = this.characters[0];
		return txtOffset+1;
	}
}
