package com.whiplash.res;

/**
 * This enum stores keys for icon sizes.
 * @author Matthias Thimm
 */
public enum WlIconSize {

	/** The size 16x16. */
	SIZE_16x16 (16,16,"16x16"),
	/** The size 24x24. */
	SIZE_24x24 (24,24,"24x24"),
	/** The size 32x32. */
	SIZE_32x32 (32,32,"32x32"),
	/** The size 48x48. */
	SIZE_48x48 (48,48,"48x48");
	
	/** The actual size string. */
	private String sizeString;
	/** The width and height as integers. */
	private int width, height;
	
	/** Creates a new size with the given size string.
	 * @param sizeString a size string.
	 */
	WlIconSize(int width, int height, String sizeString){
		this.width = width;
		this.height = height;
		this.sizeString = sizeString;
	}
	
	/**
	 * @return The height.
	 */
	public int getHeight(){
		return this.height;
	}
	
	/**
	 * @return The width.
	 */
	public int getWidth(){
		return this.width;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Enum#toString()
	 */
	public String toString(){
		return this.sizeString;
	}
}
