package com.github.angerona.fw.motivation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.github.angerona.fw.AgentComponent;
import com.github.angerona.fw.AngeronaPluginAdapter;
import com.github.angerona.fw.gui.UIPlugin;
import com.github.angerona.fw.gui.base.ViewComponent;
import com.github.angerona.fw.operators.BaseOperator;

/**
 * 
 * @author Manuel Barbi
 *
 */
@PluginImplementation
public class MotivationPlugin extends AngeronaPluginAdapter implements UIPlugin {

	@Override
	public List<Class<? extends AgentComponent>> getAgentComponentImpl() {
		List<Class<? extends AgentComponent>> components = new ArrayList<>();
		return components;
	}

	@Override
	public List<Class<? extends BaseOperator>> getOperators() {
		List<Class<? extends BaseOperator>> operators = new ArrayList<>();
		return operators;
	}

	@Override
	public Map<String, Class<? extends ViewComponent>> getUIComponents() {
		Map<String, Class<? extends ViewComponent>> views = new HashMap<>();
		return views;
	}

}
