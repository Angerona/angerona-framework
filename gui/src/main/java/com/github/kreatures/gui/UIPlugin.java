package com.github.kreatures.gui;

import java.util.Map;

import com.github.kreatures.core.KReaturesPlugin;
import com.github.kreatures.gui.base.ViewComponent;

/**
 * A UI-Plugin extending the functionality of the KReatures UI Extension library
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
