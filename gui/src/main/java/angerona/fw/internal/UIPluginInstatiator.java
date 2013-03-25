package angerona.fw.internal;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.AngeronaPlugin;
import angerona.fw.gui.UIPlugin;
import angerona.fw.gui.view.View;
import angerona.fw.listener.PluginAdapter;

/**
 * This class is responsible to hold the implementations provided by the UI plugins.
 * It acts as PluginListener to bet informed if a plugin is loaded and then tests if
 * the plugin also contains UI compoents if this is the case it saves the implementation
 * classes for the views for later use.
 * 
 * Like it information provider PluginInstantiator this class implements the Singleton
 * pattern.
 * 
 * @author Tim Janus
 */
public class UIPluginInstatiator extends PluginAdapter {
	/** reference to logger implementation */
	private Logger LOG = LoggerFactory.getLogger(UIPluginInstatiator.class);
	
	/** map containing registered views some of them are default other might be provided by plugins */
	private Map<String, Class<? extends View>> viewMap = new HashMap<String, Class<? extends View>>();
	
	public Map<String, Class<? extends View>> getViewMap() {
		return Collections.unmodifiableMap(viewMap);
	}
	
	@Override
	public void loadedPlugin(AngeronaPlugin plugin) {
		if(plugin instanceof UIPlugin) {
			UIPlugin uiPlugin = (UIPlugin) plugin;
			
			for(String viewName : uiPlugin.getUIComponents().keySet()) {
				Class<? extends View> impl = uiPlugin.getUIComponents().get(viewName);
				viewMap.put(viewName, impl);
				PluginInstantiator.getInstance().onRegistered(View.class, impl);
				LOG.info("Registered UI Implementation '{}' for '{}'.", impl.getName(), View.class.getName());
			}
		}
	}
	
	@Override
	public void unloadingPlugin(AngeronaPlugin plugin) {
		if(plugin instanceof UIPlugin) {
			UIPlugin uiPlugin = (UIPlugin) plugin;
			
			for(String viewName : uiPlugin.getUIComponents().keySet()) {
				Class<? extends View> impl = uiPlugin.getUIComponents().get(viewName);
				viewMap.remove(viewName);
				PluginInstantiator.getInstance().onUnregistered(View.class, impl);
				LOG.info("Unregistered UI Implementation '{}' for '{}'.", impl.getName(), View.class.getName());
			}
		}
	}
	
	private static UIPluginInstatiator mInstance;
	
	public static UIPluginInstatiator getInstance() {
		if(mInstance == null) {
			mInstance = new UIPluginInstatiator();
		}
		return mInstance;
	}
}
