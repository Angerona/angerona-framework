package com.github.angerona.fw.internal;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.angerona.fw.AngeronaPlugin;
import com.github.angerona.fw.gui.UIPlugin;
import com.github.angerona.fw.gui.base.EntityViewComponent;
import com.github.angerona.fw.gui.base.ViewComponent;
import com.github.angerona.fw.listener.PluginAdapter;

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
	private Map<String, Class<? extends ViewComponent>> viewMap = new HashMap<String, Class<? extends ViewComponent>>();
	
	private Map<String, Class<? extends EntityViewComponent>> entityViewMap = new HashMap<>();
	
	public Map<String, Class<? extends ViewComponent>> getViewMap() {
		return Collections.unmodifiableMap(viewMap);
	}
	
	public Map<String, Class<? extends EntityViewComponent>> getEntityViewMap() {
		return Collections.unmodifiableMap(entityViewMap);
	}
	
	@Override
	public void loadedPlugin(AngeronaPlugin plugin) {
		if(plugin instanceof UIPlugin) {
			UIPlugin uiPlugin = (UIPlugin) plugin;
			
			for(String viewName : uiPlugin.getUIComponents().keySet()) {
				Class<? extends ViewComponent> impl = uiPlugin.getUIComponents().get(viewName);
				
				boolean isEntityView = false;
				Class<?> cls = impl;
				while(cls != null) {
					if(cls.equals(EntityViewComponent.class)) {
						isEntityView = true;
						break;
					}
					cls = cls.getSuperclass();
				}
				
				Class<?> base = isEntityView ? EntityViewComponent.class : ViewComponent.class;
				if(isEntityView) {
					@SuppressWarnings("unchecked")
					Class<? extends EntityViewComponent> cImpl = (Class<? extends EntityViewComponent>)impl;
					entityViewMap.put(viewName, cImpl);
				}
				else
					viewMap.put(viewName, impl);
				PluginInstantiator.getInstance().onRegistered(base, impl);
				LOG.info("Registered UI Implementation '{}' for '{}'.", impl.getName(), base.getName());
			}
		}
	}
	
	@Override
	public void unloadingPlugin(AngeronaPlugin plugin) {
		if(plugin instanceof UIPlugin) {
			UIPlugin uiPlugin = (UIPlugin) plugin;
			
			for(String viewName : uiPlugin.getUIComponents().keySet()) {
				Class<? extends ViewComponent> impl = uiPlugin.getUIComponents().get(viewName);
				viewMap.remove(viewName);
				PluginInstantiator.getInstance().onUnregistered(ViewComponent.class, impl);
				LOG.info("Unregistered UI Implementation '{}' for '{}'.", impl.getName(), ViewComponent.class.getName());
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
