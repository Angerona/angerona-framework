package angerona.fw;

/**
 * Plugin listener gets informed if the PluginInstantiator reloads its implementations.
 * 
 * @author Tim Janus
 */
public interface PluginListener {
	void loadingImplementations(PluginInstantiator pi);
}
