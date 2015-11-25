package com.github.kreaturesfw.gui.project;

import javax.swing.AbstractButton;
import javax.swing.JComponent;

import com.github.kreaturesfw.core.AngeronaProject;
import com.github.kreaturesfw.core.serialize.Resource;
import com.github.kreaturesfw.gui.base.View;
import com.github.kreaturesfw.gui.util.CollectionMonitor;
import com.github.kreaturesfw.gui.util.DefaultUserObjectWrapper;
import com.github.kreaturesfw.gui.util.UserObjectWrapper;

/**
 * An interface for views for the Angerona project.
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
	void onProjectChange(AngeronaProject project);
	
	public static class DefaultUserObjectFactory implements UserObjectFactory {

		@Override
		public UserObjectWrapper createUserObject(Resource res) {
			return new DefaultUserObjectWrapper(res);
		}
		
	}
}
