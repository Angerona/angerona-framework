package com.whiplash.gui;

/**
 * Classes implementing this interface provide methods to handle
 * actions coming from a search panel.
 * @author Matthias Thimm
 */
public interface SearchHandler{
	
	/** Whether a forward search is to be performed. */
	public final int FORWARD_SEARCH = 1;
	/** Whether a backward search is to be performed. */
	public final int BACKWARD_SEARCH = 2;
	/** Indicates that the phrase has been found. */
	public final int PHRASE_FOUND = 3;
	/** Indicates that the phrase has not been found. */
	public final int PHRASE_NOT_FOUND = 4;
	/** Indicates that the phrase has been found but the search has to 
	 * be continued from the top of the file. */
	public final int CONTINUED_FROM_TOP = 5;
	/** Indicates that the phrase has been found but the search has to 
	 * be continued from the bottom of the file. */
	public final int CONTINUED_FROM_BOTTOM = 6;
	
	/** This method is to be called when the search panel is to be closed. */
	public void regainFocus();
	
	/** Performs a search in the given direction (one of FORWARD_SEARCH, BACKWARD_SEARCH).
	 * @param searchText the text to be searched for.
	 * @param whether the search should be case sensitive.
	 * @param direction one of FORWARD_SEARCH, BACKWARD_SEARCH.
	 * @return one of PHRASE_FOUND, PHRASE_NOT_FOUND, CONTINUED_FROM_TOP, CONTINUED_FROM_BOTTOM.
	 */
	public int searchText(String searchText, boolean matchCase, int direction);
}
