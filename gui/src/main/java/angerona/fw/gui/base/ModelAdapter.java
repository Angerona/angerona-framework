package angerona.fw.gui.base;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Base class for GUI models implementing the Model interface and
 * providing protected method to fire property change events.
 * Internally the PropertyChangeSupport class is used as implementation and
 * therefore the property change implementation is thread safe.
 * 
 * @author Tim Janus
 */
public class ModelAdapter implements Model {

    private PropertyChangeSupport propertyChangeSupport;

    public ModelAdapter()
    {
        propertyChangeSupport = new PropertyChangeSupport(this);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    protected void firePropertyChange(String propertyName, Object oldValue, Object newValue) {
        propertyChangeSupport.firePropertyChange(propertyName, oldValue, newValue);
    }

    protected void fireIndexedPropertyChange(String propertyName, int index, Object oldValue, Object newValue) {
    	propertyChangeSupport.fireIndexedPropertyChange(propertyName, index, oldValue, newValue);
    }
}
