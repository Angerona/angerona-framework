package com.github.kreaturesfw.defendingagent;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.github.kreaturesfw.core.AgentComponent;
import com.github.kreaturesfw.core.AngeronaPluginAdapter;
import com.github.kreaturesfw.core.operators.BaseOperator;
import com.github.kreaturesfw.defendingagent.gui.HistoryView;
import com.github.kreaturesfw.defendingagent.gui.ViewView;
import com.github.kreaturesfw.defendingagent.operators.def.GenerateOptionsOperator;
import com.github.kreaturesfw.defendingagent.operators.def.SubgoalGenerationOperator;
import com.github.kreaturesfw.defendingagent.operators.def.UpdateBeliefsOperator;
import com.github.kreaturesfw.defendingagent.operators.def.ViolatesOperator;
import com.github.kreaturesfw.gui.UIPlugin;
import com.github.kreaturesfw.gui.base.ViewComponent;

@PluginImplementation
/**
 * The DefendingAgentPlugin adds a ViewComponent to track conditional beliefs
 * held by the attacker as well as a SpeechAct for revision requests.
 *  
 * @author Pia Wierzoch, Sebastian Homann
 */
public class DefendingAgentPlugin extends AngeronaPluginAdapter implements UIPlugin {

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
