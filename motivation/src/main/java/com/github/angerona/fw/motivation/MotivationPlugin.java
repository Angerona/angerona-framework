package com.github.angerona.fw.motivation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import com.github.angerona.fw.AgentComponent;
import com.github.angerona.fw.AngeronaPluginAdapter;
import com.github.angerona.fw.EnvironmentBehavior;
import com.github.angerona.fw.gui.UIPlugin;
import com.github.angerona.fw.gui.base.ViewComponent;
import com.github.angerona.fw.island.DynamicIslandBehavior;
import com.github.angerona.fw.island.IslandTranslator;
import com.github.angerona.fw.island.IslandUpdateBeliefsOperator;
import com.github.angerona.fw.island.StaticIslandBehavior;
import com.github.angerona.fw.island.comp.Area;
import com.github.angerona.fw.island.comp.Battery;
import com.github.angerona.fw.logic.BaseTranslator;
import com.github.angerona.fw.motivation.behavior.BasicBehavior;
import com.github.angerona.fw.motivation.dao.impl.LevelWeights;
import com.github.angerona.fw.motivation.dao.impl.MotStructure;
import com.github.angerona.fw.motivation.dao.impl.MotiveCouplings;
import com.github.angerona.fw.motivation.dao.impl.WeightRanges;
import com.github.angerona.fw.motivation.operators.MotivationOperator;
import com.github.angerona.fw.motivation.operators.temp.ActionSelectionOperator;
import com.github.angerona.fw.motivation.operators.temp.FilterOperator;
import com.github.angerona.fw.motivation.plans.impl.TrailOperator;
import com.github.angerona.fw.motivation.reliable.impl.ActionSequences;
import com.github.angerona.fw.motivation.reliable.impl.TimeSlots;
import com.github.angerona.fw.motivation.view.CouplingsView;
import com.github.angerona.fw.motivation.view.MotStructureView;
import com.github.angerona.fw.motivation.view.RangesView;
import com.github.angerona.fw.motivation.view.WeightsView;
import com.github.angerona.fw.operators.BaseOperator;

/**
 * {@link MotivationPlugin} introduces the basic operator to integrate the motivation component into the framework. as well as the depending
 * {@link AgentComponent}s and {@link ViewComponent}s.
 * 
 * @author Manuel Barbi
 * 
 */
@PluginImplementation
public class MotivationPlugin extends AngeronaPluginAdapter implements UIPlugin {

	@Override
	public List<Class<? extends AgentComponent>> getAgentComponentImpl() {
		List<Class<? extends AgentComponent>> reval = new ArrayList<>();
		reval.add(MotiveCouplings.class);
		reval.add(WeightRanges.class);
		reval.add(LevelWeights.class);
		reval.add(MotStructure.class);
		reval.add(ActionSequences.class);
		reval.add(TimeSlots.class);

		reval.add(Area.class);
		reval.add(Battery.class);
		return reval;
	}

	@Override
	public Map<String, Class<? extends ViewComponent>> getUIComponents() {
		Map<String, Class<? extends ViewComponent>> reval = new HashMap<String, Class<? extends ViewComponent>>();
		reval.put("MotiveCoupling-View", CouplingsView.class);
		reval.put("WeightRange-View", RangesView.class);
		reval.put("LevelWeight-View", WeightsView.class);
		reval.put("MotStructure-View", MotStructureView.class);
		return reval;
	}

	@Override
	public List<Class<? extends BaseOperator>> getOperators() {
		List<Class<? extends BaseOperator>> reval = new ArrayList<>();
		reval.add(MotivationOperator.class);

		reval.add(TrailOperator.class);
		reval.add(FilterOperator.class);
		reval.add(ActionSelectionOperator.class);

		reval.add(IslandUpdateBeliefsOperator.class);
		return reval;
	}

	@Override
	public List<Class<? extends EnvironmentBehavior>> getEnvironmentBehaviors() {
		List<Class<? extends EnvironmentBehavior>> reval = new ArrayList<>();
		reval.add(BasicBehavior.class);

		reval.add(StaticIslandBehavior.class);
		reval.add(DynamicIslandBehavior.class);
		return reval;
	}

	@Override
	public List<Class<? extends BaseTranslator>> getTranslatorImpl() {
		List<Class<? extends BaseTranslator>> reval = new ArrayList<>();
		reval.add(IslandTranslator.class);
		return reval;
	}

}
