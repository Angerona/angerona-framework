package com.github.angerona.knowhow;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.github.angerona.fw.AgentComponent;
import com.github.angerona.fw.AngeronaPluginAdapter;
import com.github.angerona.fw.gui.UIPlugin;
import com.github.angerona.fw.gui.base.ViewComponent;
import com.github.angerona.fw.operators.BaseOperator;
import com.github.angerona.knowhow.asp.KnowhowASP;
import com.github.angerona.knowhow.graph.KnowhowGraph;
import com.github.angerona.knowhow.graph.KnowhowGraphSubgoal;
import com.github.angerona.knowhow.gui.KnowhowBaseView;
import com.github.angerona.knowhow.gui.KnowhowGraphView;
import com.github.angerona.knowhow.situation.SituationStorage;

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
		return reval;
	}



}
