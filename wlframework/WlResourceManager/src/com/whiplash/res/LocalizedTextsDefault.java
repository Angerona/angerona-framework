package com.whiplash.res;

import java.util.*;

/**
 * This class provides default English translations for texts.
 * @author Matthias Thimm
 */
public abstract class LocalizedTextsDefault {

	/** Returns the localized texts.
	 * @return the localized texts.
	 */
	public static Map<WlText,String> getLocalizedTexts(){
		Map<WlText,String> texts = new HashMap<WlText,String>();		
		texts.put(WlText.CLOSE, "Close");
		texts.put(WlText.OPEN, "Open");
		texts.put(WlText.OPEN_DOTS, "Open…");
		texts.put(WlText.OPENFILE, "Open file…");
		texts.put(WlText.NEW, "New");
		texts.put(WlText.EDIT, "Edit");
		texts.put(WlText.SHOW_MORE, "Show more…");
		texts.put(WlText.CANCEL, "Cancel");
		texts.put(WlText.DONT_SAVE, "Don't Save");
		texts.put(WlText.SAVE, "Save");
		texts.put(WlText.SAVEAS, "Save as");
		texts.put(WlText.SAVEAS_DOTS, "Save As…");
		texts.put(WlText.QUESTION, "Question");
		texts.put(WlText.DIALOG_SAVECHANGES, "<html><b>Do you want to save changes to this document<br>before closing?</b><p>If you don't save, your changes will be lost.</html>");
		texts.put(WlText.JUST_SET_NEW_ENCODING, "Just set new encoding");
		texts.put(WlText.RELOAD, "Reload");
		texts.put(WlText.ENCODING, "Encoding");
		texts.put(WlText.UNTITLED, "untitled");
		texts.put(WlText.FILE, "File");
		texts.put(WlText.PREVIOUS, "Previous");
		texts.put(WlText.NEXT, "Next");
		texts.put(WlText.TEXT, "Text");
		texts.put(WlText.FIND, "Find");
		texts.put(WlText.FINDNEXT, "Find Next");
		texts.put(WlText.MATCHCASE, "Match case");
		texts.put(WlText.CUT, "Cut");
		texts.put(WlText.COPY, "Copy");
		texts.put(WlText.PASTE, "Paste");
		texts.put(WlText.UNDO, "Undo");
		texts.put(WlText.REDO, "Redo");
		texts.put(WlText.SELECT_ALL, "Select All");
		texts.put(WlText.FONT_BIGGER, "Increase font size");
		texts.put(WlText.FONT_SMALLER, "Decrease font size");
		texts.put(WlText.LINE_WRAP, "Soft line wrap");
		texts.put(WlText.GENERAL, "General");
		texts.put(WlText.TYPESETTING, "Typesetting");
		texts.put(WlText.OPENRECENT, "Open Recent");
		texts.put(WlText.CLEARHISTORY, "Clear History");
		texts.put(WlText.VIEW, "View");
		texts.put(WlText.WINDOW, "Window");
		texts.put(WlText.MINIMIZE_WINDOW, "Minimize");
		texts.put(WlText.MINIMIZE_WINDOW_ALL, "Minimize All");
		texts.put(WlText.ZOOM, "Zoom");
		texts.put(WlText.BRING_ALL_TO_FRONT, "Bring All to Front");
		texts.put(WlText.HELP, "Help");
		texts.put(WlText.FONTS_AND_COLORS, "Fonts & Colors");
		texts.put(WlText.PREF_DISPLAY_FONT, "Display font:");
		texts.put(WlText.PREF_LINE_WRAP, "Soft line wrap (default setting):");
		texts.put(WlText.DIALOG_CHANGEENCODING, "<html><b>Do you want to reload the file using the new encoding<br>or just set a new encoding for later savings?</b><p>If you choose to reload the file, your changes will be lost.</html>");
		texts.put(WlText.DIALOG_FILECOULDNOTBEOPENED, "The given file could not be opened: ");
		texts.put(WlText.DIALOG_FILECOULDNOTBESAVED, "The given file could not be saved: ");
		texts.put(WlText.INFO_TYPE_TO_SEARCH, "Type to search...");
		texts.put(WlText.INFO_PHRASE_NOT_FOUND, "Phrase not found");
		texts.put(WlText.INFO_REACHED_END_OF_DOCUMENT_CONTINUED_TOP, "Reached end of file, continued from beginning");
		texts.put(WlText.INFO_REACHED_END_OF_DOCUMENT_CONTINUED_BOTTOM, "Reached end of file, continued from end");
		texts.put(WlText.PREF_TITLE_DEFAULT_ENCODING, "Default encoding:");
		texts.put(WlText.PREF_LENGTH_RECENT_FILES, "Length of recent files list:");
		texts.put(WlText.PREF_TITLE_DEFAULT_ENCODING, "Default encoding:");
		texts.put(WlText.PREF_FILE_EXPLORER_FILES, "File explorer files:");		
		texts.put(WlText.FILE_EXPLORER_TITLE, "File Explorer");
		return texts;
	}
}
