package angerona.fw.gui.base;

import java.beans.PropertyChangeListener;

/**
 * Basis interface for GUI model classes providing the
 * methods to add PropertyChangeListeners.
 * @author Tim Janus
 */
public interface Model {
	/**
	 * Registers the given listener for property changes
	 * @param listener	Reference to the listener
	 */
	void addPropertyChangeListener(PropertyChangeListener listener);
	
	/**
	 * Unregisters the given listener for property changes
	 * @param listener	Reference to the listener
	 */
	void removePropertyChangeListener(PropertyChangeListener listener);
}
