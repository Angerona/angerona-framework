package com.github.kreatures.gui.util;

import java.util.LinkedList;
import java.util.List;

import javax.swing.JComponent;

/**
 * A CollectionMonitor monitors a collection of user objects which are
 * saved in Java UI elements like JTree or JList. It generalizes the selection
 * model of the used Java UI element by providing a list of user objects assuming the
 * items of the Java UI elements use the UserObjectWrapper as user objects. It also
 * informs CollectionMonitorListener about remove invocations so that a Presenter
 * can react to the event and use the CollectionMonitor instance to determine which
 * user objects have to be removed from the data model.
 * 
 * @author Tim Janus
 *
 * @param <T>	The type of the Java UI element holding the collection of user objects.
 */
public abstract class CollectionMonitor<T extends JComponent> {
	
	/**
	 * This listener got informed about an remove invocation, when the user presses 
	 * DELETE for example.
	 * 
	 * @author Tim Janus
	 */
	public static interface CollectionMonitorListener {
		/** is called when a remove event occurs, the user pressed DELETE for example. */
		void invokeRemove(JComponent sender);
	}
	
	/**
	 * the listeners registered to this controller, beware most of the 
	 * events are handled by the UserObjectWrapper and not by the listener
	 */
	private List<CollectionMonitorListener> listeners = new LinkedList<>();
	
	/** the component which is controlled by this controller */
	protected T component;
	
	/**
	 * Changes the component which is monitored, unwires
	 * the events from the old component and wires them
	 * to the new component.
	 * @param component	Reference to the component
	 */
	public void setComponent(T component) {
		if(this.component != component) {
			if(this.component != null)
				unwireEvents();
			this.component = component;
			if(this.component != null)
				wireEvents();
		}
	}
	
	/**
	 * Registers the given listener for CollectionMonitor events.
	 * @param listener
	 */
	public void addListener(CollectionMonitorListener listener) {
		listeners.add(listener);
	}
	
	/**
	 * Unregisters the given listener for CollectionMonitor events.
	 * @param listener
	 * @return
	 */
	public boolean removeListener(CollectionMonitorListener listener) {
		return listeners.remove(listener);
	}
	
	/**
	 * Fires the remove event by informing all registered CollectionMontiorListener.
	 */
	protected void fireRemoveEvent() {
		for(CollectionMonitorListener listener : listeners) {
			listener.invokeRemove(component);
		}
	}
	
	/**
	 * Searches for selected items in the monitored JComponent and fetches their user
	 * objects, if the user objects are of type UO they are returned.
	 * @param cls	The class defining the type of the elements in the returned list
	 * @return		A list containing all selected user objects of type UO.
	 */
	public <UO> List<UO> getSelectedUserObjectsOfType(Class<UO> cls) {
		List<UO> reval = new LinkedList<>();
		List<Object> userObjects = getSelectedUserObjects();
		for(Object obj : userObjects) {
			if(cls.isAssignableFrom(obj.getClass())) {
				@SuppressWarnings("unchecked")
				UO cobj = (UO)obj;
				reval.add(cobj);
			}
		}
		return reval;
	}
	
	/**
	 * @return All user objects of selected items in the monitored JComponent.
	 */
	public abstract List<Object> getSelectedUserObjects();
	
	/**
	 * Wires all the events which are received from the monitored JComponent. It
	 * is called by the setComponent() method.
	 */
	protected abstract void wireEvents();
	
	/**
	 * Unwires all the events which are received from the monitored JComponent. It
	 * is called by the setComponent() method.
	 */
	protected abstract void unwireEvents();
}
