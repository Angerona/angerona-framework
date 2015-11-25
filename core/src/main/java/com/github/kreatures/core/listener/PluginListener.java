package com.github.kreatures.core.listener;

import com.github.kreatures.core.KReaturesPlugin;


/**
 * PluginListener gets informed if the PluginInstantiator loads implementations.
 * 
 * @author Tim Janus
 */
public interface PluginListener {
	/**
	 * called when a plugin is loading, before any implementationRegistered method
	 * is called.
	 * @param plugin	The plugin instance.
	 */
	void loadingPlugin(KReaturesPlugin plugin);
	
	/**
	 * Called when the loading of plugin is finished. It is called after the last
	 * implementationRegistered invocation for the plugin.
	 * @param plugin	The plugin instance
	 */
	void loadedPlugin(KReaturesPlugin plugin);
	
	/**
	 * Called when an implementation is registered.
	 * @param base		The base class of the implementation
	 * @param impl		The class of the implementation.
	 */
	void implementationRegistered(Class<?> base, Class<?> impl);
	
	/**
	 * called when a plugin is unloading, before any implementationUnregistered method
	 * is called.
	 * @param plugin	The plugin instance.
	 */
	void unloadingPlugin(KReaturesPlugin plugin);
	
	/**
	 * Called when the unloading of a plugin is finished. It is called after the last
	 * implementationUnregistered invocation for the plugin.
	 * @param plugin	The plugin instance
	 */
	void unloadedPlugin(KReaturesPlugin plugin);
	
	/**
	 * Called when an implementation is unregistered.
	 * @param base		The base class of the implementation
	 * @param impl		The class of the implementation.
	 */
	void implementationUnregistered(Class<?> base, Class<?> impl);
}
