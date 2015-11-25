package com.github.kreatures.gui.base;

import java.util.Map;

/**
 * Adapter class implementing the View interface of the MVP pattern by
 * adding methods which do nothing. This allows the sub classes to only
 * overload those method of the different observer interfaces they have
 * to use. Therefore ViewAdapter acts as base class for views which are 
 * no Panels like menus etc. See {@link ObservingPanel} for a base class
 * for panels acting as View.
 * 
 * @author Tim Janus
 */
public abstract class ViewAdapter implements View {

	@Override
	public <T> boolean allowPropertyChange(String propertyName, T oldValue,
			T newValue) {
		return true;
	}

	@Override
	public <T> T transformPropertyChange(String propertyName, T oldValue,
			T newValue) {
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
		/// does nothing
	}

	@Override
	public void onClear(String mapName) {
		// does nothing
	}

}
