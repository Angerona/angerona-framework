package com.github.kreatures.secrecy;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.github.kreatures.core.AgentComponent;
import com.github.kreatures.core.KReaturesPluginAdapter;
import com.github.kreatures.secrecy.components.SecrecyKnowledge;
import com.github.kreatures.secrecy.gui.SecrecyView;
import com.github.kreatures.gui.UIPlugin;
import com.github.kreatures.gui.base.ViewComponent;

/**
 * Provides AgentComponent implementations like the SecrecyKnowledge and a UI View
 * for the SecrecyKnowledge
 * @author Tim Janus
 */
@PluginImplementation
public class SecrecyPlugin extends KReaturesPluginAdapter
	implements 
	UIPlugin {

	@Override
	public List<Class<? extends AgentComponent>> getAgentComponentImpl() {
		List<Class<? extends AgentComponent>> reval = new LinkedList<>();
		reval.add(SecrecyKnowledge.class);
		return reval;
	}

	@Override
	public Map<String, Class<? extends ViewComponent>> getUIComponents() {
		Map<String, Class<? extends ViewComponent>> reval = new HashMap<String, Class<? extends ViewComponent>>();
		reval.put("Confidential-Knowledge", SecrecyView.class);
		return reval;
	}

}
