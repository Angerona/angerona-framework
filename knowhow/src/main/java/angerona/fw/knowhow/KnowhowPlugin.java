package angerona.fw.knowhow;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import angerona.fw.AgentComponent;
import angerona.fw.AgentPlugin;
import angerona.fw.gui.UIComponent;
import angerona.fw.gui.UIPlugin;
import angerona.fw.knowhow.gui.KnowhowUIComponent;

@PluginImplementation
public class KnowhowPlugin implements AgentPlugin, UIPlugin {

	public Map<String, Class<? extends UIComponent>> getUIComponents() {
		Map<String, Class<? extends UIComponent>> reval = new HashMap<String, Class<? extends UIComponent>>();
		reval.put("Knowhow", KnowhowUIComponent.class);
		return reval;
	}

	public List<Class<? extends AgentComponent>> getAgentComponents() {
		List<Class<? extends AgentComponent>> reval = new LinkedList<Class<? extends AgentComponent>>();
		reval.add(KnowhowBase.class);
		return reval;
	}

}
