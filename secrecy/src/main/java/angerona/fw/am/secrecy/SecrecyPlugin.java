package angerona.fw.am.secrecy;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import angerona.fw.AgentComponent;
import angerona.fw.am.secrecy.components.SecrecyKnowledge;
import angerona.fw.am.secrecy.gui.SecrecyView;
import angerona.fw.gui.UIPlugin;
import angerona.fw.gui.view.View;
import angerona.fw.internal.AgentPlugin;

@PluginImplementation
public class SecrecyPlugin 
	implements 
	AgentPlugin, 
	UIPlugin {

	@Override
	public List<Class<? extends AgentComponent>> getAgentComponents() {
		List<Class<? extends AgentComponent>> reval = new LinkedList<>();
		reval.add(SecrecyKnowledge.class);
		return reval;
	}

	@Override
	public Map<String, Class<? extends View>> getUIComponents() {
		Map<String, Class<? extends View>> reval = new HashMap<String, Class<? extends View>>();
		reval.put("Confidential-Knowledge", SecrecyView.class);
		return reval;
	}

}
