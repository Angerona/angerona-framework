package angerona.fw.knowhow;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import angerona.fw.AgentComponent;
import angerona.fw.gui.UIPlugin;
import angerona.fw.gui.view.BaseView;
import angerona.fw.internal.AgentPlugin;
import angerona.fw.internal.OperatorPlugin;
import angerona.fw.knowhow.gui.KnowhowView;
import angerona.fw.operators.BaseGenerateOptionsOperator;
import angerona.fw.operators.BaseIntentionUpdateOperator;
import angerona.fw.operators.BaseSubgoalGenerationOperator;
import angerona.fw.operators.BaseUpdateBeliefsOperator;
import angerona.fw.operators.BaseViolatesOperator;

@PluginImplementation
public class KnowhowPlugin implements 
	  AgentPlugin
	, UIPlugin
	, OperatorPlugin {

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
	public List<Class<? extends BaseUpdateBeliefsOperator>> getSupportedChangeOperators() {
		return new LinkedList<>();
	}

	@Override
	public List<Class<? extends BaseIntentionUpdateOperator>> getSupportedFilterOperators() {
		return new LinkedList<>();
	}

	@Override
	public List<Class<? extends BaseGenerateOptionsOperator>> getSupportedGenerateOptionsOperators() {
		return new LinkedList<>();
	}

	@Override
	public List<Class<? extends BaseViolatesOperator>> getSupportedViolatesOperators() {
		return new LinkedList<>();
	}

	@Override
	public List<Class<? extends BaseSubgoalGenerationOperator>> getSupportedPlaners() {
		List<Class<? extends BaseSubgoalGenerationOperator>> reval = new LinkedList<>();
		reval.add(KnowhowSubgoal.class);
		return reval;
	}

}
