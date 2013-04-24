package angerona.fw.am.secrecy;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import angerona.fw.AgentComponent;
import angerona.fw.AngeronaPluginAdapter;
import angerona.fw.am.secrecy.components.SecrecyKnowledge;
import angerona.fw.am.secrecy.gui.SecrecyView;
import angerona.fw.gui.UIPlugin;
import angerona.fw.gui.base.ViewComponent;

/**
 * Provides AgentComponent implementations like the SecrecyKnowledge and a UI View
 * for the SecrecyKnowledge
 * @author Tim Janus
 */
@PluginImplementation
public class SecrecyPlugin extends AngeronaPluginAdapter
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
