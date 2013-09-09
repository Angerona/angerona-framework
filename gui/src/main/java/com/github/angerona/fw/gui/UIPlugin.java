package com.github.angerona.fw.gui;

import java.util.Map;

import com.github.angerona.fw.AngeronaPlugin;
import com.github.angerona.fw.gui.base.ViewComponent;

/**
 * A UI-Plugin extending the functionality of the Angerona UI Extension library
 * by adding more views to the framework.
 * @author Tim Janus
 */
public interface UIPlugin extends AngeronaPlugin {
	/**
	 * @return 	a map containing the names of the views as key and the class object of
	 * 			the actual view implementation as value.
	 */
	Map<String, Class<? extends ViewComponent>> getUIComponents();
}
