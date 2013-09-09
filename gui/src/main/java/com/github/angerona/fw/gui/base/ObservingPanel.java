package com.github.angerona.fw.gui.base;

import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JPanel;

public class ObservingPanel extends JPanel implements View {

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

	@Override
	public <K, V> void onPut(String mapName, Map<K, V> changes) {
		// does nothing
	}

	@Override
	public <K> void onRemove(String mapName, K key) {
		// does nothing
	}

	@Override
	public void onClear(String mapName) {
		// does nothing
	}

	@Override
	public JComponent getRootComponent() {
		return this;
	}

}
