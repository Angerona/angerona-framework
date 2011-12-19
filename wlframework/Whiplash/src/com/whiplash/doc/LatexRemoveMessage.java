package com.whiplash.doc;

/**
 * This is a removal message meant for a model handler to inform its model.
 * @author Matthias Thimm
 */
public class LatexRemoveMessage extends LatexUpdateMessage {

	/** The offset of the removal. */
	private int offset;
	/** The length of the removal. */
	private int len;
	
	/** Creates a new remove message.
	 * @param offs some offset.
	 * @param len some length
	 */
	public LatexRemoveMessage(int offs, int len){
		this.offset = offs;
		this.len = len;
	}
	
	/** Returns the offset of the removal.
	 * @return the offset of the removal.
	 */
	public int getOffset(){
		return this.offset;
	}
	
	/** Returns the length of the removal.
	 * @return the length of the removal.
	 */
	public int getLength(){
		return this.len;
	}
}
