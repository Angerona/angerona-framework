package com.whiplash.res;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.*;

import javax.swing.*;

/**
 * This is a default resource manager which looks for its resources in some
 * specified package and expects some given subfolder structure.
 * @author Matthias Thimm
 */
public class DefaultResourceManager extends WlResourceManager {
	
	/** The path (package) to the resources. */
	private URL resourceRoot;
	/** A map for the local texts. */
	private Map<WlText,String> localTexts;
	
	/** Creates a new resource manager which's resources
	 * lie below the given resource root. For text translations
	 * the default setting is used.
	 * @param resourceRoot the root resource
	 */
	public DefaultResourceManager(URL resourceRoot){
		this.resourceRoot = resourceRoot;
		this.localTexts = LocalizedTextsDefault.getLocalizedTexts();
	}
	
	/** Creates a new resource manager which's resources
	 * lie below the given resource root. 
	 * @param resourceRoot the root resource
	 * @param localTexts a map for the local texts.
	 */
	public DefaultResourceManager(URL resourceRoot, Map<WlText,String> localTexts){
		this.resourceRoot = resourceRoot;
		this.localTexts = localTexts;
	}
	
	/* (non-Javadoc)
	 * @see com.whiplash.res.WlResourceManager#getIcon(com.whiplash.res.WlIcon, com.whiplash.res.WlIconSize)
	 */
	public Icon getIcon(WlIcon type, WlIconSize size) throws RuntimeException{
		// if the given icon is already in the Mac toolkit retrieve it from there
		if(System.getProperty("os.name").startsWith("Mac")){
			if(type.equals(WlIcon.PREFERENCES_GENERAL)){				
				return new ImageIcon(Toolkit.getDefaultToolkit( ).getImage("NSImage://NSPreferencesGeneral").getScaledInstance(size.getWidth(), size.getWidth(), Image.SCALE_SMOOTH));
			}
		}		
		try {			
			return new ImageIcon(new URL(this.resourceRoot.toString() + File.separator + "icons" + File.separator + size.toString() + File.separator + type.toString() + ".png"));
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}

	/* (non-Javadoc)
	 * @see com.whiplash.res.WlResourceManager#getLocalizedText(com.whiplash.res.WlText)
	 */
	@Override
	public String getLocalizedText(WlText text) throws RuntimeException {
		return this.localTexts.get(text);
	}
	
}
