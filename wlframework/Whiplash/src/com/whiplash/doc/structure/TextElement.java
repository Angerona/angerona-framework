package com.whiplash.doc.structure;

import java.util.*;

/**
 * This element represents plain text in a latex document, i.e. a sequence of characters that contains
 * neither of "\", "$", "%", "&", "_", "^", "{", "}", "[", "]", "#".
 * @author Matthias Thimm
 *
 */
public class TextElement extends MultipleCharacterElement {

	/** Creates a new text element for the given document model.
	 * @param model some latex document model.
	 */
	public TextElement(LatexDocumentModel model) {
		super(model);	
	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.doc.structure.MultipleCharacterElement#consume(java.lang.String, int)
	 */
	protected int consume(String str, int offset){
		if(str.length() == offset + 1){
			this.length = 1;
			this.characters = new char[1];
			this.characters[0] = str.charAt(offset);
		}else{
			int endIndex = offset;
			char ch = str.charAt(endIndex + 1);
			while( ch != '&' && ch != '}' && ch != ']' && ch != '\\'
				&& ch != '%' && ch != '#' && ch != '$' && ch != '{'
				&& ch != '[' && ch != '_' && ch != '^' && str.length() > endIndex + 1){
				endIndex++;				
				if(str.length() == endIndex + 1) break;	
				ch = str.charAt(endIndex + 1);
			}
			this.length = endIndex - offset + 1;
			this.characters = new char[this.length];
			str.getChars(offset, endIndex + 1, this.characters, 0);
		}
		return offset + this.length;		
	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.doc.structure.MultipleCharacterElement#split(int)
	 */
	protected List<AbstractLatexElement> split(int offset){
		TextElement fst = new TextElement(this.model);
		TextElement snd = new TextElement(this.model);
		fst.length = offset - this.startOffset;
		fst.characters = this.characters;
		snd.length = this.startOffset + this.length - offset;
		snd.characters = new char[snd.length];
		System.arraycopy(this.characters, offset-this.startOffset, snd.characters, 0, snd.length);	
		List<AbstractLatexElement> result = new ArrayList<AbstractLatexElement>();
		result.add(fst);
		result.add(snd);
		return result;
	}
}
