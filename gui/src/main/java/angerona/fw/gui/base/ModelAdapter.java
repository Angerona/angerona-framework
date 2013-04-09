package angerona.fw.gui.base;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

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
