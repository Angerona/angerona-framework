package angerona.fw.gui;

import java.util.Map;

import net.xeoh.plugins.base.Plugin;

/**
 * A UI-Plugin extending the functionality of the Angerona UI Extension library.
 * @author Tim Janus
 */
public interface UIPlugin extends Plugin {
	/**
	 * TODO: Use map???
	 * @return
	 */
	Map<String, Class<? extends UIComponent>> getUIComponents();
}
