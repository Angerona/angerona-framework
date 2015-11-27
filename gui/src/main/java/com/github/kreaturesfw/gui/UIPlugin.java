package com.github.kreaturesfw.gui;

import java.util.Map;

import com.github.kreaturesfw.core.KReaturesPlugin;
import com.github.kreaturesfw.gui.base.ViewComponent;

/**
 * A UI-Plugin extending the functionality of the Angerona UI Extension library
 * by adding more views to the framework.
 * @author Tim Janus
 */
public interface UIPlugin extends KReaturesPlugin {
	/**
	 * @return 	a map containing the names of the views as key and the class object of
	 * 			the actual view implementation as value.
	 */
	Map<String, Class<? extends ViewComponent>> getUIComponents();
}
