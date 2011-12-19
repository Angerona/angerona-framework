package com.whiplash.res;

import java.util.*;

import javax.swing.*;

/**
 * Classes extending this class provide resources to the application
 * such as icons, images, and language translations 
 * @author Matthias Thimm
 *
 */
public abstract class WlResourceManager {

	/** The list of all resource manager registered in the application. */
	private static List<WlResourceManager> resourceManager = new LinkedList<WlResourceManager>();
	/** The default resource manager in the application. */
	private static WlResourceManager defaultResourceManager = null;
	
	/** Returns the default resource manager.
	 * @return the default resource manager.
	 */
	public static WlResourceManager getDefaultResourceManager(){
		return WlResourceManager.defaultResourceManager;
	}
	
	/** Sets the given resource manager as default (if the manager is not
	 * already in the application's list of resource managers, it is added).
	 * @param resourceManager a resource manager.
	 */
	public static void setDefaultResourceManager(WlResourceManager resourceManager){
		WlResourceManager.resourceManager.add(resourceManager);
		WlResourceManager.defaultResourceManager = resourceManager;
	}
	
	/** Adds the given resource manager to the applications resource managers.
	 * @param resourceManager a resource manager.
	 * @return "true" if the addition was successful.
	 */
	public static boolean addResourceManager(WlResourceManager resourceManager){
		return WlResourceManager.resourceManager.add(resourceManager);
	}
	
	/** Returns the resource managers registered in the application.
	 * @return the resource managers registered in the application.
	 */
	public List<WlResourceManager> getResourceManagers(){
		return WlResourceManager.resourceManager;
	}
	
	/** Checks whether a default resource manager is set.
	 * @return "true" iff a default resource manager is set.
	 */
	public static boolean hasDefaultResourceManager(){
		return WlResourceManager.defaultResourceManager != null;
	}
	
	/** Returns the icon identified by the given type.
	 * @param type an icon type.
	 * @param size the size of the icon.
	 * @return the request icon.
	 * @throws RuntimeException if the retrieval failed.
	 */
	public abstract Icon getIcon(WlIcon type, WlIconSize size) throws RuntimeException;
	
	/** Returns the localized text of the given identifier.
	 * @param text identifies the expected text.
	 * @return the localized text.
	 * @throws RuntimeException if the retrieval failed.
	 */
	public abstract String getLocalizedText(WlText text) throws RuntimeException;
	
}
