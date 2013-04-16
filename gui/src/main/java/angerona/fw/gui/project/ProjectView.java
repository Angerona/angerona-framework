package angerona.fw.gui.project;

import javax.swing.AbstractButton;

import angerona.fw.AngeronaProject;
import angerona.fw.gui.util.TreeHelper.DefaultUserObjectWrapper;
import angerona.fw.gui.util.TreeHelper.UserObjectWrapper;
import angerona.fw.serialize.Resource;
import angerona.fw.util.MapObserver;
import angerona.fw.util.PropertyObserver;

/**
 * An interface for views for the Angerona project.
 * @author Tim Janus
 */
public interface ProjectView extends PropertyObserver, MapObserver {	
	
	public static interface UserObjectFactory {
		UserObjectWrapper createUserObject(Resource res);
	}
	
	void setUserObjectFactory(UserObjectFactory factory);
	
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
