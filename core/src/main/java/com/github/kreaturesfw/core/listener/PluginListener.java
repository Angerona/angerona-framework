package com.github.kreaturesfw.core.listener;

import com.github.kreaturesfw.core.AngeronaPlugin;


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
	void loadingPlugin(AngeronaPlugin plugin);
	
	/**
	 * Called when the loading of plugin is finished. It is called after the last
	 * implementationRegistered invocation for the plugin.
	 * @param plugin	The plugin instance
	 */
	void loadedPlugin(AngeronaPlugin plugin);
	
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
	void unloadingPlugin(AngeronaPlugin plugin);
	
	/**
	 * Called when the unloading of a plugin is finished. It is called after the last
	 * implementationUnregistered invocation for the plugin.
	 * @param plugin	The plugin instance
	 */
	void unloadedPlugin(AngeronaPlugin plugin);
	
	/**
	 * Called when an implementation is unregistered.
	 * @param base		The base class of the implementation
	 * @param impl		The class of the implementation.
	 */
	void implementationUnregistered(Class<?> base, Class<?> impl);
}
