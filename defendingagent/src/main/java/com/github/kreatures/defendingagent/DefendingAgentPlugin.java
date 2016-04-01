package com.github.kreatures.defendingagent;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.github.kreatures.core.AgentComponent;
import com.github.kreatures.core.KReaturesPluginAdapter;
import com.github.kreatures.defendingagent.gui.HistoryView;
import com.github.kreatures.defendingagent.gui.ViewView;
import com.github.kreatures.defendingagent.operators.def.GenerateOptionsOperator;
import com.github.kreatures.defendingagent.operators.def.SubgoalGenerationOperator;
import com.github.kreatures.defendingagent.operators.def.UpdateBeliefsOperator;
import com.github.kreatures.defendingagent.operators.def.ViolatesOperator;
import com.github.kreatures.gui.UIPlugin;
import com.github.kreatures.gui.base.ViewComponent;
import com.github.kreatures.core.operators.BaseOperator;

@PluginImplementation
/**
 * The DefendingAgentPlugin adds a ViewComponent to track conditional beliefs
 * held by the attacker as well as a SpeechAct for revision requests.
 *  
 * @author Pia Wierzoch, Sebastian Homann
 */
public class DefendingAgentPlugin extends KReaturesPluginAdapter implements UIPlugin {

	@Override
	public List<Class<? extends BaseOperator>> getOperators() {
		List<Class<? extends BaseOperator>> reval = new LinkedList<>();
		reval.add(GenerateOptionsOperator.class);
		reval.add(UpdateBeliefsOperator.class);
		reval.add(SubgoalGenerationOperator.class);
		reval.add(ViolatesOperator.class);
		return reval;
	}
	
	@Override
	public List<Class<? extends AgentComponent>> getAgentComponentImpl() {
		List<Class<? extends AgentComponent>> reval = new LinkedList<Class<? extends AgentComponent>>();

		reval.add(CensorComponent.class);
		reval.add(ViewDataComponent.class);
		reval.add(HistoryComponent.class);
		return reval;
	}

	@Override
	public Map<String, Class<? extends ViewComponent>> getUIComponents() {
		Map<String, Class<? extends ViewComponent>> reval = new HashMap<String, Class<? extends ViewComponent>>();
		reval.put("Views", ViewView.class);
		reval.put("History", HistoryView.class);
		return reval;
	}

}
