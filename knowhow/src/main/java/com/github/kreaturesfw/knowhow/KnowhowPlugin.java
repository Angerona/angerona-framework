package com.github.kreaturesfw.knowhow;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.github.kreaturesfw.core.AgentComponent;
import com.github.kreaturesfw.core.AngeronaPluginAdapter;
import com.github.kreaturesfw.core.operators.BaseOperator;
import com.github.kreaturesfw.gui.UIPlugin;
import com.github.kreaturesfw.gui.base.ViewComponent;
import com.github.kreaturesfw.knowhow.asp.KnowhowASP;
import com.github.kreaturesfw.knowhow.graph.KnowhowGraph;
import com.github.kreaturesfw.knowhow.graph.KnowhowGraphSubgoal;
import com.github.kreaturesfw.knowhow.graph.KnowhowIntentionUpdate;
import com.github.kreaturesfw.knowhow.graph.KnowhowViolates;
import com.github.kreaturesfw.knowhow.gui.KnowhowBaseView;
import com.github.kreaturesfw.knowhow.gui.KnowhowGraphView;
import com.github.kreaturesfw.knowhow.situation.SituationStorage;

/**
 * The plug-in contains know-how based planner capabilites.
 * 
 * @author Tim Janus
 */
@PluginImplementation
public class KnowhowPlugin extends AngeronaPluginAdapter 
	implements UIPlugin {

	@Override
	public Map<String, Class<? extends ViewComponent>> getUIComponents() {
		Map<String, Class<? extends ViewComponent>> reval = new HashMap<String, Class<? extends ViewComponent>>();
		reval.put("Knowhow-Base", KnowhowBaseView.class);
		reval.put("Knowhow-Graph", KnowhowGraphView.class);
		return reval;
	}
	
	@Override
	public List<Class<? extends AgentComponent>> getAgentComponentImpl() {
		List<Class<? extends AgentComponent>> reval = new LinkedList<Class<? extends AgentComponent>>();
		reval.add(KnowhowBase.class);
		reval.add(KnowhowGraph.class);
		reval.add(SituationStorage.class);
		return reval;
	}
	
	@Override
	public List<Class<? extends BaseOperator>> getOperators() {
		List<Class<? extends BaseOperator>> reval = new LinkedList<>();
		reval.add(KnowhowASP.class);
		reval.add(KnowhowGraphSubgoal.class);
		reval.add(KnowhowIntentionUpdate.class);
		reval.add(KnowhowViolates.class);
		return reval;
	}



}
