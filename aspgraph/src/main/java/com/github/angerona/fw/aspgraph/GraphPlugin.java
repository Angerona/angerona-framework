package com.github.angerona.fw.aspgraph;

import java.util.HashMap;
import java.util.Map;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import angerona.fw.aspgraph.view.GraphView;
import angerona.fw.gui.UIPlugin;
import angerona.fw.gui.view.BaseView;

@PluginImplementation
public class GraphPlugin implements UIPlugin{

	public Map<String, Class<? extends BaseView>> getUIComponents() {
		Map<String, Class<? extends BaseView>> reval = new HashMap<String, Class<? extends BaseView>>();
		reval.put("Asp-Graph", GraphView.class);
		return reval;
	}

}
