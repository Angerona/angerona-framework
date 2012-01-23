package angerona.fw.gui;

import java.util.Map;

import net.xeoh.plugins.base.Plugin;


public interface UIPlugin extends Plugin {
	Map<String, Class<? extends BaseComponent>> getUIComponents();
}
