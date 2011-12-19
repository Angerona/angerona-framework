package com.whiplash.gui;

import java.awt.*;
import java.io.*;

import javax.swing.*;

import com.whiplash.res.*;

/**
 * This class provides some methods for popup dialogs.
 * @author Matthias Thimm
 */
public abstract class WlDialog {

	/** Constant indicating that the "Save" button has been pressed. */
	public static final int SAVE_ACTION = 0;
	/** Constant indicating that the "Don't Save" button has been pressed. */
	public static final int DONT_SAVE_ACTION = 1;
	/** Constant indicating that the "Cancel" button has been pressed. */
	public static final int CANCEL_ACTION = 2;
	/** Constant indicating that the "Reload" button has been pressed. */
	public static final int RELOAD_ACTION = 3;
	/** Constant indicating that the "Set" button has been pressed. */
	public static final int SET_ACTION = 4;
	
	/** Shows the user an error message with the given text and
	 * a single "Ok" button.
	 * @param text a string.
	 * @param parentComponent the parent component relative to which the dialog will be shown.
	 */
	public static void showErrorMessage(String text, Component parentComponent) {
		JOptionPane.showMessageDialog(parentComponent, text, "Error", JOptionPane.ERROR_MESSAGE);
	}
	
	/** Shows the user a confirmation dialog whether to save a changed file with
	 * "Save", "Don't save", "Cancel" buttons. It returns one of
	 * SAVE_ACTION, DONT_SAVE_ACTION, CANCEL_ACTION.   
	 * @param parentComponent the parent component relative to which the dialog will be shown.
	 * @return one of SAVE_ACTION, DONT_SAVE_ACTION, CANCEL_ACTION.
	 */
	public static int showUnsavedChangesDialog(WlComponent parentComponent){
		// check resource manager
		if(!WlResourceManager.hasDefaultResourceManager())			
			throw new RuntimeException("No default resource manager set.");
		WlResourceManager resourceManager = WlResourceManager.getDefaultResourceManager();
		Object[] options = {resourceManager.getLocalizedText(WlText.SAVE), resourceManager.getLocalizedText(WlText.CANCEL), resourceManager.getLocalizedText(WlText.DONT_SAVE)};
		int answer =  JOptionPane.showOptionDialog(parentComponent, 
				resourceManager.getLocalizedText(WlText.DIALOG_SAVECHANGES),
				resourceManager.getLocalizedText(WlText.QUESTION),JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,options[0]);
		if(answer == 0)
			return WlDialog.SAVE_ACTION;
		else if(answer == 2)
			return WlDialog.DONT_SAVE_ACTION;
		return WlDialog.CANCEL_ACTION;
	}
	
	/** Shows the user a confirmation dialog triggered when selecting a new
	 * encoding for a given file. The user is asked whether to reload the file
	 * with the given encoding or to save it with the new encoding.
	 * "Save", "Don't save", "Cancel" buttons. It returns one of
	 * SET_ACTION, RELOAD_ACTION, CANCEL_ACTION.   
	 * @param parentComponent the parent component relative to which the dialog will be shown.
	 * @return one of SEt_ACTION, RELOAD_ACTION, CANCEL_ACTION.
	 */
	public static int showChangeEncoding(WlComponent parentComponent){
		// check resource manager
		if(!WlResourceManager.hasDefaultResourceManager())			
			throw new RuntimeException("No default resource manager set.");
		WlResourceManager resourceManager = WlResourceManager.getDefaultResourceManager();
		Object[] options = {resourceManager.getLocalizedText(WlText.JUST_SET_NEW_ENCODING), resourceManager.getLocalizedText(WlText.CANCEL), resourceManager.getLocalizedText(WlText.RELOAD)};
		int answer =  JOptionPane.showOptionDialog(parentComponent,
				resourceManager.getLocalizedText(WlText.DIALOG_CHANGEENCODING),
				resourceManager.getLocalizedText(WlText.QUESTION),JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,options[0]);
		if(answer == 0)
			return WlDialog.SET_ACTION;
		else if(answer == 2)
			return WlDialog.RELOAD_ACTION;
		return WlDialog.CANCEL_ACTION;
	}
	
	/** Shows a file chooser dialog for saving a file and returns the selected file
	 * @param parentComponent the parent component relative to which the dialog will be shown.
	 * @param defaultFileExtension the extension for the new file. 
	 * @return a file or "null" iff the user canceled the operation.
	 */
	public static File showSaveFileDialog(WlComponent parentComponent, String defaultFileExtension){
		FileDialog fileDialog = new FileDialog(parentComponent.getParentPane().getWindow(), "Save file...", FileDialog.SAVE);		
		fileDialog.setVisible(true);
		if(fileDialog.getFile() == null)
			return null;
		return new File(fileDialog.getDirectory() + File.separator + fileDialog.getFile() + defaultFileExtension);
	}
	
	/** Shows a file chooser dialog for saving a file and returns the selected file
	 * @param window the window relative to which the dialog will be shown. 
	 * @return a file or "null" iff the user canceled the operation.
	 */
	public static File showOpenFileDialog(WlWindow window){
		// check resource manager
		if(!WlResourceManager.hasDefaultResourceManager())			
			throw new RuntimeException("No default resource manager set.");
		WlResourceManager resourceManager = WlResourceManager.getDefaultResourceManager();
		FileDialog fileDialog = new FileDialog(window, resourceManager.getLocalizedText(WlText.OPENFILE), FileDialog.LOAD);		
		fileDialog.setVisible(true);
		if(fileDialog.getFile() == null)
			return null;
		return new File(fileDialog.getDirectory() + File.separator + fileDialog.getFile());
	}
}
