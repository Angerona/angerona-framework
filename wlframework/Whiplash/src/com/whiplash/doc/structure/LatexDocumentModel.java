package com.whiplash.doc.structure;

import java.util.*;
import java.util.List;

import com.sun.tools.javac.util.*;
import com.whiplash.doc.*;
import com.whiplash.util.*;

/**
 * Instances of this class maintain a logical model of
 * a Latex document.
 * @author Matthias Thimm
 */
public class LatexDocumentModel {

	/** The document this model represents. */
	private LatexDocument document;
	
	/** The latex elements of this element. */
	private List<AbstractLatexElement> children;
	
	/** The length of this document. */
	private int length;
	
	/** Creates a new Latex model for the given document.
	 * @param document The document this model represents.
	 */
	public LatexDocumentModel(LatexDocument document){
		this.document = document;
		this.children = new LabelTreeList<AbstractLatexElement>();
		this.length = 0;
	}
	
	/** Returns the document of this model.
	 * @return the document of this model.
	 */
	public LatexDocument getDocument(){
		return this.document;
	}
	
	/** Removes the given text segment from this model and returns an event which contains
	 * the changes made. 
	 * @param offs some offset.
	 * @param len some length.
	 * @return the changes of the operation.
	 */
	public LatexElementChange remove(int offs, int len){
		this.length -= len;
		//TODO
		//TODO modify offsets of positions and elements		
		return null;
	}
	
	/** Returns the length of this model.
	 * @return the length of this model.
	 */
	public int getLength(){
		return this.length;
	}
	
	/** Inserts the given string into this model and returns an event
	 * which contains the changes made.
	 * @param offset some offset
	 * @param str some text.
	 * @return the changes of the operation.
	 */
	public LatexElementChange insertString(int offset, String str){
		// the change record for this insert operation
		LatexElementChange mainChangeRecord = new LatexElementChange(this);
		// if the string to be inserted is longer than one character do
		// a complete parsing of the string. If the string has only character
		// perform a more efficient insertion (except this document is empty).
		if(str.length() > 1 || this.children.isEmpty()){
			List<AbstractLatexElement> elements = this.parseString(str);
			if(this.children.isEmpty()){
				this.children = elements;
				int startOffset = 0;
				for(AbstractLatexElement element: this.children){
					element.startOffset = startOffset;
					startOffset += element.getLength();
				}
				mainChangeRecord.addAddedChildren(elements);
			}else{
				int startIndex = -1;
				int endIndex = 0;
				int updateIndex = -1;
				int mainChangeIndex = -1;
				if(offset == 0){
					endIndex = elements.size()-1;
					elements.addAll(this.children);
					this.children = elements;
					updateIndex = 0;
					mainChangeIndex = 0;
				}else if(offset == this.length){
					startIndex = this.children.size()-1;
					mainChangeIndex = startIndex;
					updateIndex = this.children.size();
					this.children.addAll(elements);					
				}else{
					int idx = this.getElementIndex(offset);
					mainChangeIndex = idx;
					updateIndex = idx;
					AbstractLatexElement element = this.children.get(idx);
					if(offset == element.startOffset){
						for(int i = elements.size()-1; i >= 0; i--)
							this.children.add(idx, elements.get(i));
						startIndex = idx-1;
						endIndex = idx + elements.size() - 1;
					}else{
						// now we are in the middle of some multiple character element
						MultipleCharacterElement mcElement = (MultipleCharacterElement) element;
						List<AbstractLatexElement> splits = mcElement.split(offset);
						this.children.remove(idx);
						mainChangeRecord.addRemovedChild(mcElement);
						for(int i = splits.size()-1; i >= 0; i--)
							this.children.add(idx, splits.get(i));
						for(int i = elements.size()-1; i >= 0; i--)
							this.children.add(idx+1, elements.get(i));
						startIndex = idx-1;
						endIndex = idx + elements.size() + splits.size();
					}
				}
				// collapse/split elements starting from startIndex and maybe endIndex
				int lastModifiedIndex = mainChangeIndex;
				if(startIndex > -1){
					Pair<Integer,Integer> idx = this.collapseAndSplit(startIndex);
					endIndex += idx.snd;
					if(idx.fst > endIndex)
						endIndex = -1;
					lastModifiedIndex = idx.fst;
				}
				if(endIndex > -1){
					Pair<Integer,Integer> idx = this.collapseAndSplit(endIndex);
					lastModifiedIndex = idx.fst;
				}
				for(int i = mainChangeIndex; i <= lastModifiedIndex; i++)
					mainChangeRecord.addAddedChild(this.children.get(i));				
				// update element offsets
				if(updateIndex > -1){
					int newOffset = 0;
					if(updateIndex > 0)
						newOffset = this.children.get(updateIndex - 1).startOffset + this.children.get(updateIndex - 1).getLength(); 
					for(int i = updateIndex; i < this.children.size(); i++){
						this.children.get(i).setStartOffset(newOffset);
						newOffset += this.children.get(i).getLength();
					}
				}
			}			
		}else{
			// note, that this.children is not empty
			//TODO something more efficient			
			
			//TODO modify offsets of elements
		}		
		return mainChangeRecord;
	}
	
	/** Checks if the element at the given index can be joined
	 * with the element at the given index plus one (or part of it).
	 * Appropriately splits elements if necessary. This process is
	 * continued until no elements can be joined any more.
	 * @param idx the index from which to start collapsing and splitting.
	 * @return a pair of two integer, the first element is the index of the last modified child
	 *  (-1 if no child was modified), the second the difference of the number of children of
	 *  this element to the number before beginning to collapse and split.
	 */
	private Pair<Integer,Integer> collapseAndSplit(int idx){
		AbstractLatexElement element = (AbstractLatexElement) this.children.get(idx);
		if(element instanceof SingleCharacterElement)
			return new Pair<Integer,Integer>(-1,0);
		AbstractLatexElement element2 = this.children.get(idx + 1);
		if(element instanceof TextElement){			
			if(element2 instanceof TextElement){
				((TextElement) element).appendElement((TextElement)element2);
				this.children.remove(idx + 1);
				return new Pair<Integer,Integer>(idx,-1);
			}
			return new Pair<Integer,Integer>(-1,0);
		}
		if(element instanceof CommandElement){
			if(element2 instanceof SingleCharacterElement){
				if(((CommandElement)element).length > 1)
					return new Pair<Integer,Integer>(-1,0);
				((CommandElement)element).appendElement((SingleCharacterElement)element2);
				this.children.remove(idx + 1);
				return new Pair<Integer,Integer>(idx,-1);			
			}
			if(element2 instanceof CommentElement){
				if(((CommandElement)element).length > 1)
					return new Pair<Integer,Integer>(-1,0);
				((CommandElement)element).appendChar('%');
				List<AbstractLatexElement> elements = this.parseString(new String(((CommentElement) element2).characters,1,((CommentElement) element2).length-1));
				this.children.remove(idx + 1);
				for(int i = elements.size()-1; i >= 0; i--)
					this.children.add(idx + 1, elements.get(i));
				Pair<Integer,Integer> subIdx = this.collapseAndSplit(idx + elements.size());
				return new Pair<Integer,Integer>(subIdx.fst, elements.size() - 1 + subIdx.snd);
			}
			if(element2 instanceof CommandElement){
				if(((CommandElement)element).length > 1)
					return new Pair<Integer,Integer>(-1,0);
				((CommandElement)element).appendChar('\\');
				this.children.remove(idx + 1);
				if(((CommandElement) element2).length == 1)					
					return new Pair<Integer,Integer>(idx, -1);
				List<AbstractLatexElement> elements = this.parseString(new String(((CommandElement) element2).characters,1,((CommandElement) element2).length-1));
				for(int i = elements.size()-1; i >= 0; i--)
					this.children.add(idx + 1, elements.get(i));
				Pair<Integer,Integer> subIdx = this.collapseAndSplit(idx + elements.size());
				return new Pair<Integer,Integer>(subIdx.fst, elements.size() - 1 + subIdx.snd);
			}
			if(element2 instanceof TextElement){
				if(((CommandElement)element).length > 1){
					if(!Character.isLetter(((CommandElement)element).characters[1]))
						return new Pair<Integer,Integer>(-1,0);
					int letterCount;
					for(letterCount = 0; letterCount < ((TextElement)element2).length && Character.isLetter(((TextElement)element2).characters[letterCount]); letterCount++)
						;
					if(letterCount == 0)
						return new Pair<Integer,Integer>(-1,0);
					((CommandElement)element).appendCharacters(((TextElement)element2).characters, 0, letterCount);
					if(((TextElement)element2).length == letterCount){
						this.children.remove(idx + 1);
						return new Pair<Integer,Integer>(idx, -1);
					}
					((TextElement)element2).removeCharacters(0, letterCount);
					return new Pair<Integer,Integer>(idx + 1, 0);
				}	
				if(((TextElement)element2).characters[0] == '\n'){
					return new Pair<Integer,Integer>(-1, 0);
				}
				if(!Character.isLetter(((TextElement)element2).characters[0])){
					((CommandElement)element).appendChar(((TextElement)element2).characters[0]);
					((TextElement)element2).removeCharacters(0, 1);
					return new Pair<Integer,Integer>(idx + 1, 0);
				}
				int letterCount;
				for(letterCount = 0; letterCount < ((TextElement)element2).length && Character.isLetter(((TextElement)element2).characters[letterCount]); letterCount++)
					;
				if(letterCount == 0)
					return new Pair<Integer,Integer>(-1,0);
				((CommandElement)element).appendCharacters(((TextElement)element2).characters, 0, letterCount);
				if(((TextElement)element2).length == letterCount){
					this.children.remove(idx + 1);
					return new Pair<Integer,Integer>(idx, -1);
				}
				((TextElement)element2).removeCharacters(0, letterCount);
				return new Pair<Integer,Integer>(idx + 1, 0);
			}
		}
		if(element instanceof CommentElement){
			if(((CommentElement) element).isClosed())
				return new Pair<Integer,Integer>(-1, 0);
			int lastIndex = idx+1;
			while(lastIndex < this.children.size()-1 && !this.children.get(lastIndex).containsCharacter('\n'))
				lastIndex++;
			int diffChildrenCount = 0;
			for(int i = idx + 1; i < lastIndex - 1; i++){
				((CommentElement)element).appendCharacters(this.children.get(idx + 1).characters, 0, this.children.get(idx + 1).getLength());
				this.children.remove(idx + 1);
				diffChildrenCount--;
			}
			element2 = this.children.get(lastIndex);
			if(element2.containsCharacter('\n')){
				if(element2 instanceof CommentElement){
					((CommentElement)element).appendElement((CommentElement)element2);
					this.children.remove(lastIndex);
					diffChildrenCount--;
					return new Pair<Integer,Integer>(lastIndex-1, diffChildrenCount);
				}
				// element2 must be a text element
				int lnIdx = element2.indexOfCharacter('\n');
				if(lnIdx == element2.getLength()-1){
					((CommentElement)element).appendElement((TextElement)element2);
					this.children.remove(lastIndex);
					diffChildrenCount--;
					return new Pair<Integer,Integer>(lastIndex-1, diffChildrenCount);
				}
				((CommentElement)element).appendCharacters(element2.characters, 0, lnIdx+1);
				((TextElement)element2).removeCharacters(0, lnIdx+1);
				return new Pair<Integer,Integer>(lastIndex, diffChildrenCount);
			}
			((CommentElement)element).appendCharacters(element2.characters, 0, element2.getLength());
			this.children.remove(lastIndex);
			diffChildrenCount--;
			return new Pair<Integer,Integer>(lastIndex-1, diffChildrenCount);
		}
		// this should not happen
		throw new RuntimeException("Fatal error: exhausted possible cases.");
	}
	
	/** Parses the given string into a sequence of latex elements.
	 * @param str some string.
	 * @return a list of latex elements.
	 */
	protected List<AbstractLatexElement> parseString(String str){
		List<AbstractLatexElement> elements = new LinkedList<AbstractLatexElement>();
		AbstractLatexElement element;
		int idx = 0;
		while(idx < str.length()){
			element = null;
			if(str.charAt(idx) == '&') element = new AmpersandElement(this);
			else if(str.charAt(idx) == '}') element = new ClosedCurlyBracketElement(this);
			else if(str.charAt(idx) == ']') element = new ClosedSquareBracketElement(this);
			else if(str.charAt(idx) == '#') element = new HashmarkElement(this);
			else if(str.charAt(idx) == '$') element = new MathDelimiterElement(this);
			else if(str.charAt(idx) == '{') element = new OpenCurlyBracketElement(this);
			else if(str.charAt(idx) == '[') element = new OpenSquareBracketElement(this);
			else if(str.charAt(idx) == '_') element = new SubscriptElement(this);
			else if(str.charAt(idx) == '^') element = new SuperscriptElement(this);
			if(element != null){
				idx++;
				elements.add(element);
				continue;
			}
			if(str.charAt(idx) == '\\') element = new CommandElement(this);
			else if(str.charAt(idx) == '%')	element = new CommentElement(this);				
			else element = new TextElement(this);
			idx = ((MultipleCharacterElement)element).consume(str, idx);
			elements.add(element);			
		}
		return elements;
	}


	/* (non-Javadoc)
	 * @see javax.swing.text.Element#getElementIndex(int)
	 */
	public int getElementIndex(int offset) {
		if(this.children.size() == 0)
			return -1;
		if(offset <= 0)
			return 0;
		if(this.length <= offset)
			return this.children.size()-1;
		// do a binary search
		int beginIdx = 0;
		int endIdx = this.children.size()-1;
		int centerIdx, currentOffset;
		while(beginIdx != endIdx){
			centerIdx = (endIdx + beginIdx) / 2;
			currentOffset = this.children.get(centerIdx).startOffset;
			if(currentOffset > offset)
				endIdx = centerIdx - 1;
			else beginIdx = centerIdx;
		}		
		return beginIdx;		
	}
}
