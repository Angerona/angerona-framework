package com.whiplash.control;

import javax.swing.*;

import com.whiplash.res.*;

/**
 * This class provides several action commands that can occur in the GUI. 
 * @author Matthias Thimm
 */
public abstract class GuiActionCommands {
	/** Action command for adding a new file to the GUI. */
	public static final String ACTION_NEW_FILE = "NEW-FILE";
	/** Action command for opening a file. */
	public static final String ACTION_OPEN_FILE = "OPEN-FILE";
	/** Action command for opening a file from the "open recent" menu. */
	public static final String ACTION_OPEN_RECENT_FILE = "OPEN-RECENT-FILE";
	/** Action command for clearing the recent files list. */
	public static final String ACTION_CLEAR_RECENT_HISTORY = "CLEAR-RECENT-HISTORY";
	/** Action command for closing the current file. */
	public static final String ACTION_CLOSE_FILE = "CLOSE-FILE";
	/** Action command for saving the current file. */
	public static final String ACTION_SAVE_FILE = "SAVE-FILE";
	/** Action command for saving a file under a new name. */
	public static final String ACTION_SAVE_AS_FILE = "SAVE-AS-FILE";
	/** Action command for saving a file under a new name. */
	public static final String ACTION_CHANGE_ENCODING = "CHANGE-ENCODING";
	/** Action command for cut. */
	public static final String ACTION_CUT = "CUT";
	/** Action command for copy. */
	public static final String ACTION_COPY = "COPY";
	/** Action command for paste. */
	public static final String ACTION_PASTE = "PASTE";
	/** Action command for undo. */
	public static final String ACTION_UNDO = "UNDO";
	/** Action command for redo. */
	public static final String ACTION_REDO = "REDO";
	/** Action command for select all. */
	public static final String ACTION_SELECT_ALL = "SELECT-ALL";
	/** Action command for increasing display font size. */
	public static final String ACTION_FONT_BIGGER = "FONT-BIGGER";
	/** Action command for decreasing display font size. */
	public static final String ACTION_FONT_SMALLER = "FONT-SMALLER";
	/** Action command for toggling line wrap. */
	public static final String ACTION_TOGGLE_LINE_WRAP = "TOGGLE-LINE-WRAP";
	/** Action command for viewing the file explorer. */
	public static final String ACTION_SHOW_FILE_EXPLORER = "SHOW-FILE-EXPLORER";
	/** Action command for minimizing the current window. */
	public static final String ACTION_MINIMZE_WINDOW = "MINIMIZE-WINDOW";
	/** Action command for minimizing all windows. */
	public static final String ACTION_MINIMZE_WINDOW_ALL = "MINIMIZE-WINDOW-ALL";
	/** Action command for zooming the current window. */
	public static final String ACTION_ZOOM_WINDOW = "ZOOM-WINDOW";
	/** Action command for bringing all windows to the front. */
	public static final String ACTION_WINDOWS_TO_FRONT = "WINDOWS-TO-FRONT";
	/** Action command for showing the help. */
	public static final String ACTION_HELP = "HELP";
	
	/** Returns the active icon for the given action command.
	 * @param actionCommand a gui action command.
	 * @return the active icon for the given command.
	 */
	public static Icon getActiveIcon(String actionCommand){
		if(actionCommand.equals(GuiActionCommands.ACTION_NEW_FILE))
			return WlResourceManager.getDefaultResourceManager().getIcon(WlIcon.NEWFILE_ACTIVE, WlIconSize.SIZE_24x24);
		if(actionCommand.equals(GuiActionCommands.ACTION_OPEN_FILE))
			return WlResourceManager.getDefaultResourceManager().getIcon(WlIcon.OPEN_ACTIVE, WlIconSize.SIZE_24x24);
		if(actionCommand.equals(GuiActionCommands.ACTION_SAVE_FILE))
			return WlResourceManager.getDefaultResourceManager().getIcon(WlIcon.SAVE_ACTIVE, WlIconSize.SIZE_24x24);
		if(actionCommand.equals(GuiActionCommands.ACTION_SAVE_AS_FILE))
			return WlResourceManager.getDefaultResourceManager().getIcon(WlIcon.SAVEAS_ACTIVE, WlIconSize.SIZE_24x24);
		throw new IllegalArgumentException("Unknown action command '" + actionCommand + "'");
	}
	
	/** Returns the inactive icon for the given action command.
	 * @param actionCommand a gui action command.
	 * @return the inactive icon for the given command.
	 */
	public static Icon getInactiveIcon(String actionCommand){
		if(actionCommand.equals(GuiActionCommands.ACTION_NEW_FILE))
			return WlResourceManager.getDefaultResourceManager().getIcon(WlIcon.NEWFILE_INACTIVE, WlIconSize.SIZE_24x24);
		if(actionCommand.equals(GuiActionCommands.ACTION_OPEN_FILE))
			return WlResourceManager.getDefaultResourceManager().getIcon(WlIcon.OPEN_INACTIVE, WlIconSize.SIZE_24x24);
		if(actionCommand.equals(GuiActionCommands.ACTION_SAVE_FILE))
			return WlResourceManager.getDefaultResourceManager().getIcon(WlIcon.SAVE_INACTIVE, WlIconSize.SIZE_24x24);
		if(actionCommand.equals(GuiActionCommands.ACTION_SAVE_AS_FILE))
			return WlResourceManager.getDefaultResourceManager().getIcon(WlIcon.SAVEAS_INACTIVE, WlIconSize.SIZE_24x24);
		throw new IllegalArgumentException("Unknown action command '" + actionCommand + "'");
	}
	
	/** Returns the action name (human readable)
	 * @param actionCommand a gui action command.
	 * @return the action name (human readable)
	 */
	public static String getActionName(String actionCommand){
		// check resource manager
		if(!WlResourceManager.hasDefaultResourceManager())			
			throw new RuntimeException("No default resource manager set.");
		WlResourceManager resourceManager = WlResourceManager.getDefaultResourceManager();
		if(actionCommand.equals(GuiActionCommands.ACTION_NEW_FILE)) return resourceManager.getLocalizedText(WlText.NEW);
		if(actionCommand.equals(GuiActionCommands.ACTION_OPEN_FILE)) return resourceManager.getLocalizedText(WlText.OPEN);
		if(actionCommand.equals(GuiActionCommands.ACTION_CLOSE_FILE)) return resourceManager.getLocalizedText(WlText.CLOSE);
		if(actionCommand.equals(GuiActionCommands.ACTION_SAVE_FILE)) return resourceManager.getLocalizedText(WlText.SAVE);
		if(actionCommand.equals(GuiActionCommands.ACTION_SAVE_AS_FILE)) return resourceManager.getLocalizedText(WlText.SAVEAS);
		if(actionCommand.equals(GuiActionCommands.ACTION_CHANGE_ENCODING)) return resourceManager.getLocalizedText(WlText.ENCODING);
		throw new IllegalArgumentException("Unknown action command '" + actionCommand + "'");
	}
}
