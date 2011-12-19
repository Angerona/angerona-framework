package com.whiplash.doc.structure;

import java.util.*;

/**
 * This element represents a comment, i.e. some string beginning with "%" and ending with "\n" or EOF.
 * @author Matthias Thimm
 */
public class CommentElement extends MultipleCharacterElement {
	
	/** Creates a new comment element for the given document model.
	 * @param model some latex document model.
	 */
	public CommentElement(LatexDocumentModel model) {
		super(model);	
	}

	/* (non-Javadoc)
	 * @see com.whiplash.doc.structure.MultipleCharacterElement#consume(java.lang.String, int)
	 */
	protected int consume(String str, int offset){
		//NOTE: when this method is called it is assumed that str.charAt(offset) equals '%'
		int endIndex = str.indexOf('\n', offset);
		if(endIndex == -1) endIndex = str.length()-1;
		this.length = endIndex - offset + 1;
		this.characters = new char[this.length];
		for(int i = 0; i < this.length; i++)
			this.characters[i] = str.charAt(i + offset);		
		return offset + this.length;
	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.doc.structure.MultipleCharacterElement#split(int)
	 */
	protected List<AbstractLatexElement> split(int offset){
		CommentElement fst = new CommentElement(this.model);
		fst.length = offset - this.startOffset;
		fst.characters = this.characters;
		List<AbstractLatexElement> result = new ArrayList<AbstractLatexElement>();
		result.add(fst);		
		result.addAll(this.model.parseString(new String(this.characters,offset-this.startOffset,this.startOffset + this.length - offset)));
		return result;
	}
	
	/** Checks whether this comment is closed, i.e. whether it is terminated by '\n'.
	 * @return "true" iff this comment is closed.
	 */
	protected boolean isClosed(){
		return this.length != 0 && this.characters[this.length-1] == '\n';
	}
}
