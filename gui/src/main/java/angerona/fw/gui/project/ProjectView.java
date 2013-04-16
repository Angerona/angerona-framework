package angerona.fw.gui.project;

import javax.swing.AbstractButton;

import angerona.fw.AngeronaProject;
import angerona.fw.serialize.Resource;
import angerona.fw.util.MapObserver;
import angerona.fw.util.PropertyObserver;

/**
 * An interface for views for the Angerona project.
 * @author Tim Janus
 */
public interface ProjectView extends PropertyObserver, MapObserver {	
	
	/**
	 * This listener is used to inform the observers about
	 * resource selection changes and about activations of
	 * resources (for example by double click or when pressing
	 * enter when selected).
	 * 
	 * @author Tim Janus
	 */
	public static interface ResourceListener {
		void resourceActivated(Resource resource);
		
		void resourceSelected(Resource resource);
	}
	
	/**
	 * Sets the ResourceListener of this view.
	 * @param listener	Reference to the listener
	 */
	void setResourceListener(ResourceListener listener);
	
	/**
	 * @return The button responsible to remove the selected resource
	 */
	AbstractButton getRemoveButton();
	
	
	/**
	 * @return The button responsible to load new resources.
	 */
	AbstractButton getLoadButton();
	
	
	/**
	 * Is called if a new project is loaded for example.
	 * @param project
	 */
	void onProjectChange(AngeronaProject project);
}
