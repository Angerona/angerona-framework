package com.whiplash.editor;

import java.util.prefs.*;

import com.whiplash.config.*;
import com.whiplash.control.*;
import com.whiplash.res.*;

/**
 * This class contains the program entry point for WhiplashEditor.
 * @author Matthias Thimm
 */
public class WhiplashEditorMain {
	/** The application name. */
	private static final String APPLICATION_NAME = "WhiplashEditor";
	/** The application id (used for preferences). */
	private static final String APPLICATION_ID = "com.whiplash.editor";
	
	/**
	 * Program entry point for WhiplashEditor.
	 * @param args command line parameters for WhiplashEditor (none supported at the moment).
	 * @throws BackingStoreException 
	 */
	public static void main(String[] args) throws BackingStoreException{
		// initialize configuration
		WlConfiguration.init(WhiplashEditorMain.APPLICATION_ID);
		WhiplashEditorConfiguration.fillConfiguration();
		// initialize resource manager		
		DefaultResourceManager resourceManager = new DefaultResourceManager(WhiplashEditorMain.class.getResource("res"));
		WlResourceManager.setDefaultResourceManager(resourceManager);		
		// initialize main gui controller
		WlMultiTextGuiController guiController = new WlMultiTextGuiController();
		guiController.setProperty(GuiProperty.APPLICATION_NAME, WhiplashEditorMain.APPLICATION_NAME);			
		// perform final initializations
		guiController.init();
		// show the gui
		guiController.show();			
	}
}
