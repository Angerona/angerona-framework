package com.whiplash;

import java.util.prefs.*;

import com.whiplash.config.*;
import com.whiplash.control.*;
import com.whiplash.doc.*;
import com.whiplash.res.*;
import com.whiplash.util.*;

/**
 * This class contains the program entry point for Whiplash.
 * @author Matthias Thimm
 */
public class WhiplashMain {
	/** The application name. */
	private static final String APPLICATION_NAME = "Whiplash";
	/** The application id (used for preferences). */
	private static final String APPLICATION_ID = "com.whiplash";
	
	/**
	 * Program entry point for Whiplash.
	 * @param args command line parameters for Whiplash (none supported at the moment).
	 * @throws BackingStoreException 
	 */
	public static void main(String[] args) throws BackingStoreException{
		// initialize configuration
		WlConfiguration.init(WhiplashMain.APPLICATION_ID);
		WhiplashConfiguration.fillConfiguration();
		// initialize resource manager		
		DefaultResourceManager resourceManager = new DefaultResourceManager(WhiplashMain.class.getResource("res"));
		WlResourceManager.setDefaultResourceManager(resourceManager);		
		// initialize main gui controller
		WlMultiTextGuiController guiController = new WlMultiTextGuiController();
		guiController.setProperty(GuiProperty.APPLICATION_NAME, WhiplashMain.APPLICATION_NAME);			
		// register document controllers
		WlDocumentController<LatexDocument> latexDocumentController = new WlDocumentController<LatexDocument>(new LatexDocumentFactory(), FileExtensions.EXTENSION_LATEX);
		guiController.setStandardDocumentController(latexDocumentController);
		// perform final initializations
		guiController.init();
		// show the gui
		guiController.show();			
	}
}
