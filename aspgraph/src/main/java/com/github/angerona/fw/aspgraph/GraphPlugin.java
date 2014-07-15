package com.github.angerona.fw.aspgraph;

import java.util.HashMap;
import java.util.Map;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.github.angerona.fw.AngeronaPluginAdapter;
import com.github.angerona.fw.aspgraph.view.GraphView;
import com.github.angerona.fw.gui.UIPlugin;
import com.github.angerona.fw.gui.base.ViewComponent;
//import com.github.angerona.fw.gui.view.BaseView;

@PluginImplementation
public class GraphPlugin extends AngeronaPluginAdapter implements UIPlugin{

	public Map<String, Class<? extends ViewComponent>> getUIComponents() {
		Map<String, Class<? extends ViewComponent>> reval = new HashMap<String, Class<? extends ViewComponent>>();
		reval.put("Asp-Graph", GraphView.class);
		return reval;
	}


}
