package com.whiplash.doc;

import java.io.*;

import com.whiplash.res.*;

/** A null file is a place holder for files without a filename yet. In order
 * to distinguish these files setting the file = null is not appropriate. Use
 * this class instead.
 *  
 * @author Matthias Thimm
 */
public class NullFile extends File {

	/** For serialization. */
	private static final long serialVersionUID = 1L;
	
	/** Counter for untitled file names. */
	private static int counter = 0;
		
	/** The id of this file. */
	private int id;
	
	/** Creates a new null file. */
	public NullFile(){
		super(new Integer(NullFile.counter + 1).toString());
		this.id = NullFile.getNextId();
	}
	
	/** Returns a new id.
	 * @return some id.
	 */
	private static int getNextId(){
		return NullFile.counter++;
	}
	
	/* (non-Javadoc)
	 * @see java.io.File#getName()
	 */
	public String getName(){
		// check resource manager
		if(!WlResourceManager.hasDefaultResourceManager())			
			throw new RuntimeException("No default resource manager set.");
		WlResourceManager resourceManager = WlResourceManager.getDefaultResourceManager();
		return resourceManager.getLocalizedText(WlText.UNTITLED) + "-" + this.id;
	}
}
