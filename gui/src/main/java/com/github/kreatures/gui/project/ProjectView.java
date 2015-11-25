package com.github.kreatures.gui.project;

import javax.swing.AbstractButton;
import javax.swing.JComponent;

import com.github.kreatures.core.KReaturesProject;
import com.github.kreatures.gui.base.View;
import com.github.kreatures.gui.util.CollectionMonitor;
import com.github.kreatures.gui.util.DefaultUserObjectWrapper;
import com.github.kreatures.gui.util.UserObjectWrapper;
import com.github.kreatures.core.serialize.Resource;

/**
 * An interface for views for the KReatures project.
 * @author Tim Janus
 */
public interface ProjectView extends View {	
	
	public static interface UserObjectFactory {
		UserObjectWrapper createUserObject(Resource res);
	}
	
	/**
	 * @return The controller watching the changes of the resource collection 
	 */
	CollectionMonitor<? extends JComponent> getResourceCollectionController();
	
	/** sets the factory used to create user objects for the resource collection view */
	void setResourcesUserObjectFactory(UserObjectFactory factory);
	
	/**
	 * @return The button responsible to remove the selected resource
	 */
	AbstractButton getRemoveButton();
	
	
	/**
	 * @return The button responsible to add/load new resources.
	 */
	AbstractButton getAddButton();
	
	
	/**
	 * Is called if a new project is loaded for example.
	 * @param project
	 */
	void onProjectChange(KReaturesProject project);
	
	public static class DefaultUserObjectFactory implements UserObjectFactory {

		@Override
		public UserObjectWrapper createUserObject(Resource res) {
			return new DefaultUserObjectWrapper(res);
		}
		
	}
}
