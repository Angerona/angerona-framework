package com.whiplash.doc.structure;

import javax.swing.text.*;

/**
 * This class represents a text segment for some latex element.
 * @author Matthias Thimm
 */
public abstract class AbstractLatexElement {

	/** The document model this element is associated with. */
	protected LatexDocumentModel model;
		
	/** The sequence of characters of this element. */
	protected char[] characters;
	
	/** The start offset of this element. */
	protected int startOffset;
	
	/** Creates a new latex element for the given document.
	 * @param model some latex document model.
	 * @param parent the parent element.
	 */
	public AbstractLatexElement(LatexDocumentModel model){
		this.model = model;
		this.startOffset = 0;
	}
	
	/** Sets the start offset of this element.
	 * @param offset some offset
	 */
	protected void setStartOffset(int offset){
		this.startOffset = offset;
	}
	
	/** Adds the given value to this element's start offset.
	 * @param value some int.
	 */
	protected  void addToStartOffset(int value){
		this.startOffset += value;
	}
	
	/** Returns the number of characters in this element. 
	 * @return the number of characters in this element.
	 */
	protected abstract int getLength();
	
	/** Returns text beginning from the given offset (relative to
	 * the document) up to the given length. If the element is shorter
	 * than the requested length only the characters up to the end
	 * are returned. For performance reasons the offset must be inside the
	 * element and the length is greater zero. For this reason elements of length
	 * one can directly return its single character.
	 * @param offset some offset (relative to the document)
	 * @param length some int
	 * @return the requested text
	 */
	public abstract String getText(int offset, int length);
	
	/** Writes text beginning from the given offset (relative to
	 * the document) up to the given length into the given segment. If the element is shorter
	 * than the requested length only the characters up to the end
	 * are returned. For performance reasons the offset must be inside the
	 * element and the length is greater zero. For this reason elements of length
	 * one can directly return its single character.<br>
	 * NOTE: this method assumes that txt.isPartialReturn() is true and no copying
	 * of characters is performed.
	 * @param offset some offset (relative to the document)
	 * @param length some int
	 * @param txt the segment where the text is provided in.
	 * @see com.whiplash.doc.TextFileDocument#getText(int, int, javax.swing.text.Segment)
	 */
	public abstract void getText(int offset, int length, Segment txt);
	
	/** Writes text beginning from the given offset (relative to
	 * the document) up to the given length into the given segment. If the element is shorter
	 * than the requested length only the characters up to the end
	 * are returned. For performance reasons the offset must be inside the
	 * element and the length is greater zero. For this reason elements of length
	 * one can directly return its single character.<br>
	 * NOTE: this method assumes that txt.isPartialReturn() is false and copies characters
	 * into the (initialized) array of txt beginning at position txtOffset. 
	 * @param offset some offset (relative to the document)
	 * @param length some int
	 * @param txt the segment where the text is provided in.
	 * @return the offset where this method stopped writing (either because the element ended
	 * or the length was exhausted.
	 * @see com.whiplash.doc.TextFileDocument#getText(int, int, javax.swing.text.Segment)
	 */
	public abstract int getText(int offset, int length, Segment txt, int txtOffset);
	
	/** Checks whether this element contains the given character.
	 * @param ch some char
	 * @return "true" iff this element contains the given character.
	 */
	protected boolean containsCharacter(char ch){
		return this.indexOfCharacter(ch) != -1;
	}
	
	/** Returns the index (relative to this element) of the 
	 * first occurrence of the given character (or -1 if the character
	 * is not contained in this element).
	 * @param ch some char.
	 * @return the index of the first occurrence of the given character (or -1 otherwise)
	 */
	protected abstract int indexOfCharacter(char ch);

	/* (non-Javadoc)
	 * @see javax.swing.text.Element#getEndOffset()
	 */
	public abstract int getEndOffset();

	/* (non-Javadoc)
	 * @see javax.swing.text.Element#getStartOffset()
	 */
	public int getStartOffset() {
		return this.startOffset;
	}
}
