package angerona.fw.gui;

import java.util.Map;

import angerona.fw.AngeronaPlugin;
import angerona.fw.gui.view.View;

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
	Map<String, Class<? extends View>> getUIComponents();
}
