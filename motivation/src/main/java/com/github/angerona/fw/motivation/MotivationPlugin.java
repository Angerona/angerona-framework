package com.github.angerona.fw.motivation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.github.angerona.fw.AgentComponent;
import com.github.angerona.fw.AngeronaPluginAdapter;
import com.github.angerona.fw.gui.UIPlugin;
import com.github.angerona.fw.gui.base.ViewComponent;
import com.github.angerona.fw.motivation.dao.impl.LevelWeights;
import com.github.angerona.fw.motivation.dao.impl.MotStructure;
import com.github.angerona.fw.motivation.dao.impl.MotiveCouplings;
import com.github.angerona.fw.motivation.dao.impl.WeightRanges;
import com.github.angerona.fw.motivation.operators.IslandReliabilityOperator;
import com.github.angerona.fw.motivation.operators.MotivationOperator;
import com.github.angerona.fw.motivation.view.CouplingsView;
import com.github.angerona.fw.motivation.view.MotStructureView;
import com.github.angerona.fw.motivation.view.RangesView;
import com.github.angerona.fw.motivation.view.WeightsView;
import com.github.angerona.fw.operators.BaseOperator;

/**
 * 
 * @author Manuel Barbi
 *
 */
@PluginImplementation
public class MotivationPlugin extends AngeronaPluginAdapter implements UIPlugin {

	@Override
	public List<Class<? extends AgentComponent>> getAgentComponentImpl() {
		List<Class<? extends AgentComponent>> components = new ArrayList<>();
		components.add(MotiveCouplings.class);
		components.add(WeightRanges.class);
		components.add(LevelWeights.class);
		components.add(MotStructure.class);
		return components;
	}

	@Override
	public List<Class<? extends BaseOperator>> getOperators() {
		List<Class<? extends BaseOperator>> operators = new ArrayList<>();
		operators.add(IslandReliabilityOperator.class);
		operators.add(MotivationOperator.class);
		return operators;
	}

	@Override
	public Map<String, Class<? extends ViewComponent>> getUIComponents() {
		Map<String, Class<? extends ViewComponent>> views = new HashMap<>();
		views.put("MotiveCouplingView", CouplingsView.class);
		views.put("WeightRangesView", RangesView.class);
		views.put("LevelWeightsView", WeightsView.class);
		views.put("MotStructureView", MotStructureView.class);
		return views;
	}

}
