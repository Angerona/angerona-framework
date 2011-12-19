package com.whiplash.doc;

/**
 * This is a insertion message meant for a model handler to inform its model.
 * @author Matthias Thimm
 */
public class LatexInsertMessage extends LatexUpdateMessage {

	/** The offset of the insertion. */
	private int offset;
	/**The text for the insertion. */
	private String str;
	
	/** Creates a new insert message.
	 * @param offset some offset
	 * @param str some string.
	 */
	public LatexInsertMessage(int offset, String str){
		this.offset = offset;
		this.str = str;
	}
		
	/** Returns the offset of the insertion.
	 * @return the offset of the insertion.
	 */
	public int getOffset(){
		return this.offset;
	}
	
	/** Returns the text for the insertion.
	 * @return the text for the insertion.
	 */
	public String getString(){
		return this.str;
	}
}
