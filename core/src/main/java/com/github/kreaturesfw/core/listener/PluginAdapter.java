package com.github.kreaturesfw.core.listener;

import com.github.kreaturesfw.core.AngeronaPlugin;

/**
 * Adapts the PluginListener interface, implemented for ease of use.
 * @author Tim Janus
 */
public class PluginAdapter implements PluginListener {

	@Override
	public void loadingPlugin(AngeronaPlugin plugin) {}

	@Override
	public void loadedPlugin(AngeronaPlugin plugin) {}

	@Override
	public void implementationRegistered(Class<?> base, Class<?> impl) {}

	@Override
	public void implementationUnregistered(Class<?> base, Class<?> impl) {}

	@Override
	public void unloadingPlugin(AngeronaPlugin plugin) {}

	@Override
	public void unloadedPlugin(AngeronaPlugin plugin) {}

}
