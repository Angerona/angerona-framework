package com.github.angerona.fw.gui.project;

import javax.swing.AbstractButton;
import javax.swing.JComponent;

import com.github.angerona.fw.AngeronaProject;
import com.github.angerona.fw.gui.base.View;
import com.github.angerona.fw.gui.util.CollectionMonitor;
import com.github.angerona.fw.gui.util.DefaultUserObjectWrapper;
import com.github.angerona.fw.gui.util.UserObjectWrapper;
import com.github.angerona.fw.serialize.Resource;

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
