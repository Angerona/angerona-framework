package angerona.fw.knowhow;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import angerona.fw.AgentComponent;
import angerona.fw.gui.UIPlugin;
import angerona.fw.gui.view.View;
import angerona.fw.internal.AgentPlugin;
import angerona.fw.internal.OperatorPluginAdapter;
import angerona.fw.knowhow.gui.KnowhowView;

@PluginImplementation
public class KnowhowPlugin extends OperatorPluginAdapter implements 
	  AgentPlugin
	, UIPlugin {

	public Map<String, Class<? extends View>> getUIComponents() {
		Map<String, Class<? extends View>> reval = new HashMap<String, Class<? extends View>>();
		reval.put("Knowhow", KnowhowView.class);
		return reval;
	}

	public List<Class<? extends AgentComponent>> getAgentComponents() {
		List<Class<? extends AgentComponent>> reval = new LinkedList<Class<? extends AgentComponent>>();
		reval.add(KnowhowBase.class);
		return reval;
	}

	@Override
	protected void registerOperators() {
		registerOperator(KnowhowSubgoal.class);
	}

}
