package com.whiplash.doc.structure;

import java.util.*;

/**
 * This element represents a command in a latex document, i.e. a string beginning with
 * "\" followed by either a sequence of characters in {a-z,A-Z}, or a single other character, or nothing.   
 * @author Matthias Thimm
 */
public class CommandElement extends MultipleCharacterElement {

	/** Creates a new command element for the given document model.
	 * @param model some latex document model.
	 */
	public CommandElement(LatexDocumentModel model) {
		super(model);	
	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.doc.structure.MultipleCharacterElement#consume(java.lang.String, int)
	 */
	protected int consume(String str, int offset){
		//NOTE: when this method is called it is assumed that str.charAt(offset) equals '\\'
		if(str.length() == offset + 1 || str.charAt(offset + 1) == '\n'){
			this.length = 1;
			this.characters = new char[1];
			this.characters[0] = '\\';
		}else{			
			if(Character.isLetter(str.charAt(offset + 1))){
				int endIndex = offset + 1;
				while(str.length() > endIndex + 1 && Character.isLetter(str.charAt(endIndex + 1)))
					endIndex++;
				this.length = endIndex - offset + 1;
				this.characters = new char[this.length];
				str.getChars(offset, endIndex + 1, this.characters, 0);
			}else{
				this.length = 2;
				this.characters = new char[2];
				this.characters[0] = '\\';
				this.characters[1] = str.charAt(offset + 1);
			}
		}		
		return offset + this.length;
	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.doc.structure.MultipleCharacterElement#split(int)
	 */
	protected List<AbstractLatexElement> split(int offset){
		CommandElement fst = new CommandElement(this.model);
		fst.length = offset - this.startOffset;
		fst.characters = this.characters;
		List<AbstractLatexElement> result = new ArrayList<AbstractLatexElement>();
		result.add(fst);		
		result.addAll(this.model.parseString(new String(this.characters,offset-this.startOffset,this.startOffset + this.length - offset)));
		return result;
	}
}
