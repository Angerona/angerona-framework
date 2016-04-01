package com.github.kreatures.knowhow;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.github.kreatures.core.AgentComponent;
import com.github.kreatures.core.KReaturesPluginAdapter;
import com.github.kreatures.gui.UIPlugin;
import com.github.kreatures.gui.base.ViewComponent;
import com.github.kreatures.core.operators.BaseOperator;
import com.github.kreatures.knowhow.asp.KnowhowASP;
import com.github.kreatures.knowhow.graph.KnowhowGraph;
import com.github.kreatures.knowhow.graph.KnowhowGraphSubgoal;
import com.github.kreatures.knowhow.graph.KnowhowIntentionUpdate;
import com.github.kreatures.knowhow.graph.KnowhowViolates;
import com.github.kreatures.knowhow.gui.KnowhowBaseView;
import com.github.kreatures.knowhow.gui.KnowhowGraphView;
import com.github.kreatures.knowhow.situation.SituationStorage;

/**
 * The plug-in contains know-how based planner capabilites.
 * 
 * @author Tim Janus
 */
@PluginImplementation
public class KnowhowPlugin extends KReaturesPluginAdapter 
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
