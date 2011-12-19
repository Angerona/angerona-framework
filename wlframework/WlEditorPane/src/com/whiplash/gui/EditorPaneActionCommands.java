package com.whiplash.gui;

/**
 * This interface provides action commands for the editor pane.
 * @author Matthias Thimm
 */
public interface EditorPaneActionCommands {
	/** Action command for opening the search panel. */
	public final String ACTION_FIND = "FIND";
	/** Action command for finding next. */
	public final String ACTION_FIND_NEXT = "FIND-NEXT";
	/** Action command for closing the search panel. */
	public final String ACTION_CLOSE_SEARCH_PANEL = "CLOSE-SEARCH-PANEL";
	/** Action command for closing the search panel. */
	public final String ACTION_NEXT_SEARCH_PANEL = "NEXT-SEARCH-PANEL";
	/** Action command for closing the search panel. */
	public final String ACTION_PREVIOUS_SEARCH_PANEL = "PREVIOUS-SEARCH-PANEL";
}
