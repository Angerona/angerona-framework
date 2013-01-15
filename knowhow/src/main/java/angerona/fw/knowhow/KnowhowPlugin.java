package angerona.fw.knowhow;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import angerona.fw.Action;
import angerona.fw.AgentComponent;
import angerona.fw.gui.UIPlugin;
import angerona.fw.gui.view.BaseView;
import angerona.fw.internal.AgentPlugin;
import angerona.fw.internal.OperatorPluginAdapter;
import angerona.fw.knowhow.gui.KnowhowView;

@PluginImplementation
public class KnowhowPlugin extends OperatorPluginAdapter implements 
	  AgentPlugin
	, UIPlugin {

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

	@Override
	public List<Class<? extends Action>> getActions() {
		return new LinkedList<>();
	}

	@Override
	protected void registerOperators() {
		registerOperator(KnowhowSubgoal.class);
	}

}
