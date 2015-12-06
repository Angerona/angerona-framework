package com.github.kreaturesfw.core.listener;

import com.github.kreaturesfw.core.KReaturesPlugin;

/**
 * Adapts the PluginListener interface, implemented for ease of use.
 * @author Tim Janus
 */
public class PluginAdapter implements PluginListener {

	@Override
	public void loadingPlugin(KReaturesPlugin plugin) {}

	@Override
	public void loadedPlugin(KReaturesPlugin plugin) {}

	@Override
	public void implementationRegistered(Class<?> base, Class<?> impl) {}

	@Override
	public void implementationUnregistered(Class<?> base, Class<?> impl) {}

	@Override
	public void unloadingPlugin(KReaturesPlugin plugin) {}

	@Override
	public void unloadedPlugin(KReaturesPlugin plugin) {}

}
