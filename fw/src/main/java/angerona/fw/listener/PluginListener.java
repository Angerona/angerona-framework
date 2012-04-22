package angerona.fw.listener;

import angerona.fw.internal.PluginInstantiator;

/**
 * Plugin listener gets informed if the PluginInstantiator reloads its implementations.
 * 
 * @author Tim Janus
 */
public interface PluginListener {
	void loadingImplementations(PluginInstantiator pi);
}
