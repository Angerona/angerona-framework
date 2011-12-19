package com.whiplash.events;

/**
 * Classes implementing this interface receive events on configuration changes.
 * @author Matthias Thimm
 */
public interface ConfigurationListener {

	/** Informs the listener that some configuration option changed its value.
	 * @param evt some event.
	 */
	public void configurationChanged(ConfigurationEvent evt);
}
