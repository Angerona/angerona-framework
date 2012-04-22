package angerona.fw.knowhow;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import angerona.fw.AgentComponent;
import angerona.fw.AgentPlugin;
import angerona.fw.gui.UIPlugin;
import angerona.fw.gui.view.BaseView;
import angerona.fw.knowhow.gui.KnowhowView;

@PluginImplementation
public class KnowhowPlugin implements AgentPlugin, UIPlugin {

	public Map<String, Class<? extends BaseView>> getUIComponents() {
		Map<String, Class<? extends BaseView>> reval = new HashMap<String, Class<? extends BaseView>>();
		reval.put("Knowhow", KnowhowView.class);
		return reval;
	}

	public List<Class<? extends AgentComponent>> getAgentComponents() {
		List<Class<? extends AgentComponent>> reval = new LinkedList<Class<? extends AgentComponent>>();
		reval.add(KnowhowBase.class);
		return reval;
	}

}
