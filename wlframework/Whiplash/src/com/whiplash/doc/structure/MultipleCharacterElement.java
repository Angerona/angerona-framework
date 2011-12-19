package com.whiplash.doc.structure;

import java.util.*;

import javax.swing.text.*;

/**
 * This element represents some sequence of characters that form a symbolic unit in a latex document.
 * @author Matthias Thimm
 */
public abstract class MultipleCharacterElement extends AbstractLatexElement {

	/** The length of valid text in the characters array. */
	protected int length;
	
	/** A locally used temporary variable, added here because of performance reasons. */
	private int copiedLength;
	
	/** Creates a new single character element for the given document model.
	 * @param document some latex document.
	 * @param parent the parent element.
	 */
	public MultipleCharacterElement(LatexDocumentModel model) {
		super(model);
		this.length = 0;
	}
		
	/** Consumes as much of the given string starting from the given offset as possible
	 * and returns the index where the consumption stopped.<br/>
	 * NOTE: when calling this method the element is assumed to be empty.
	 * @param str some string.
	 * @param offset the offset of the string from where to start.
	 * @return the index where the consumption stopped.
	 */
	protected abstract int consume(String str, int offset);
	
	/** Splits this element at the given offset into a prefix element (the element at index 0)
	 * and some suffix elements.
	 * @param offset some offset (relative to the document)
	 * @return a list of elements (the first one being the prefix element).
	 */
	protected abstract List<AbstractLatexElement> split(int offset);
	
	/* (non-Javadoc)
	 * @see com.whiplash.doc.structure.AbstractLatexElement#getLength()
	 */
	@Override
	protected int getLength() {
		return this.length;
	}
	
	/** Appends the characters of the given element to this element.<br/>
	 * NOTE: This method does not check whether the given element can 
	 * be appended without violating LaTeX syntax.
	 * @param element some element.
	 */
	protected void appendElement(MultipleCharacterElement element){
		char[] newCharacters = new char[this.length + element.length];
		System.arraycopy(this.characters, 0, newCharacters, 0, this.length);
		System.arraycopy(element.characters, 0, newCharacters, this.length, element.length);
		this.characters = newCharacters;		
		this.length = this.length + element.length;
	}
	
	/** Appends the character of the given element to this element.<br/>
	 * NOTE: This method does not check whether the given element can 
	 * be appended without violating LaTeX syntax.
	 * @param element some element.
	 */
	protected void appendElement(SingleCharacterElement element){
		char[] newCharacters = new char[this.length + 1];
		System.arraycopy(this.characters, 0, newCharacters, 0, this.length);
		newCharacters[this.length] = element.characters[0];
		this.characters = newCharacters;		
		this.length = this.length + 1;
	}
	
	/** Appends the given character to this element.<br/>
	 * NOTE: This method does not check whether the given character can 
	 * be appended without violating LaTeX syntax.
	 * @param element some element.
	 */
	protected void appendChar(char ch){
		char[] newCharacters = new char[this.length + 1];
		System.arraycopy(this.characters, 0, newCharacters, 0, this.length);
		newCharacters[this.length] = ch;
		this.characters = newCharacters;		
		this.length = this.length + 1;
	}
	
	/** Appends the given characters to this element.<br/>
	 * NOTE: This method does not check whether the given characters can 
	 * be appended without violating LaTeX syntax.
	 * @param characters some char array
	 * @param offset the offset in the char array from where to start.
	 * @param count the number of characters to append.
	 */
	protected void appendCharacters(char[] characters, int offset, int count){
		char[] newCharacters = new char[this.length + count];
		System.arraycopy(this.characters, 0, newCharacters, 0, this.length);
		System.arraycopy(characters, offset, newCharacters, this.length, count);
		this.characters = characters;
		this.length = this.length + count;
	}
	
	/** Removes the given number of characters from this element starting
	 * at the given offset.<br>
	 * NOTE: This method does not check the validity of the arguments.
	 * @param offset some offset (relative to this element)
	 * @param count some int
	 */
	protected void removeCharacters(int offset, int count){
		if(offset + count == this.length){
			this.length -= count;
			return;
		}
		System.arraycopy(this.characters, offset + count, this.characters, offset, this.length - offset - count);
		this.length -= count;
	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.doc.structure.AbstractLatexElement#indexOfCharacter(char)
	 */
	@Override
	protected int indexOfCharacter(char ch){
		for(int i = 0; i < this.length; i++)
			if(this.characters[i] == ch) 
				return i;
		return -1;
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.text.Element#getEndOffset()
	 */
	@Override
	public int getEndOffset(){
		return this.startOffset + this.length;
	}

	/* (non-Javadoc)
	 * @see com.whiplash.doc.structure.AbstractLatexElement#getText(int, int)
	 */
	public String getText(int offset, int length){
		return new String(this.characters, offset - this.startOffset, Math.min(length, this.length - offset + this.startOffset ));
	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.doc.structure.AbstractLatexElement#getText(int, int, javax.swing.text.Segment)
	 */
	public void getText(int offset, int length, Segment txt){
		txt.array = this.characters;
		txt.offset = offset - this.getStartOffset();
		txt.count = Math.min(length, this.length - offset + this.getStartOffset());			
	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.doc.structure.AbstractLatexElement#getText(int, int, javax.swing.text.Segment, int)
	 */
	public int getText(int offset, int length, Segment txt, int txtOffset){
		this.copiedLength = Math.min(length, this.length - offset + this.getStartOffset());
		System.arraycopy(this.characters, offset - this.getStartOffset(), txt.array, txtOffset, this.copiedLength);		
		return txtOffset + this.copiedLength;
	}
	
}
