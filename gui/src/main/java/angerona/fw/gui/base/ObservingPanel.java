package angerona.fw.gui.base;

import javax.swing.JPanel;

import angerona.fw.util.PropertyObserver;

public class ObservingPanel extends JPanel implements PropertyObserver{

	/** kill warning */
	private static final long serialVersionUID = 6512930047868806497L;

	@Override
	public <T> boolean allowPropertyChange(String propertyName, T oldValue,
			T newValue) {
		return true;
	}

	@Override
	public <T> T transformPropertyChange(String propertyName, T oldValue, T newValue) {
		return null;
	}

	@Override
	public <T> void propertyChange(String propertyName, T oldValue, T newValue) {
		// does nothing
	}

}
