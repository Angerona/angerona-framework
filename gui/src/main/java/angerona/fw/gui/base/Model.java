package angerona.fw.gui.base;

import java.beans.PropertyChangeListener;

public interface Model {
	void addPropertyChangeListener(PropertyChangeListener listener);
	
	void removePropertyChangeListener(PropertyChangeListener listener);
}
