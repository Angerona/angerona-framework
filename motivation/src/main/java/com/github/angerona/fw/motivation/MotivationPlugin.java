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
import com.github.angerona.fw.logic.BaseTranslator;
import com.github.angerona.fw.motivation.basic.BasicBehavior;
import com.github.angerona.fw.motivation.dao.impl.LevelWeights;
import com.github.angerona.fw.motivation.dao.impl.MotStructure;
import com.github.angerona.fw.motivation.dao.impl.MotiveCouplings;
import com.github.angerona.fw.motivation.dao.impl.WeightRanges;
import com.github.angerona.fw.motivation.island.DynamicIslandBehavior;
import com.github.angerona.fw.motivation.island.IslandTranslator;
import com.github.angerona.fw.motivation.island.StaticIslandBehavior;
import com.github.angerona.fw.motivation.island.comp.Area;
import com.github.angerona.fw.motivation.island.comp.Battery;
import com.github.angerona.fw.motivation.island.comp.IslandActions;
import com.github.angerona.fw.motivation.island.comp.TrailBasedPlans;
import com.github.angerona.fw.motivation.island.operators.BdiSelectionOperator;
import com.github.angerona.fw.motivation.island.operators.IslandActionOperator;
import com.github.angerona.fw.motivation.island.operators.IslandMotOperator;
import com.github.angerona.fw.motivation.island.operators.IslandUpdateBeliefsOperator;
import com.github.angerona.fw.motivation.island.view.AreaView;
import com.github.angerona.fw.motivation.island.view.BatteryView;
import com.github.angerona.fw.motivation.island.view.TrailPlanView;
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
		List<Class<? extends AgentComponent>> reval = new ArrayList<>();
		reval.add(MotiveCouplings.class);
		reval.add(WeightRanges.class);
		reval.add(LevelWeights.class);
		reval.add(MotStructure.class);
		reval.add(TrailBasedPlans.class);
		reval.add(IslandActions.class);
		reval.add(Battery.class);
		reval.add(Area.class);
		return reval;
	}

	@Override
	public Map<String, Class<? extends ViewComponent>> getUIComponents() {
		Map<String, Class<? extends ViewComponent>> reval = new HashMap<String, Class<? extends ViewComponent>>();
		reval.put("MotiveCoupling-View", CouplingsView.class);
		reval.put("WeightRange-View", RangesView.class);
		reval.put("LevelWeight-View", WeightsView.class);
		reval.put("MotStructure-View", MotStructureView.class);
		reval.put("Plan-View", TrailPlanView.class);
		reval.put("Area-View", AreaView.class);
		reval.put("Battery-View", BatteryView.class);
		return reval;
	}

	@Override
	public List<Class<? extends BaseOperator>> getOperators() {
		List<Class<? extends BaseOperator>> reval = new ArrayList<>();
		reval.add(MotivationOperator.class);
		reval.add(BdiSelectionOperator.class);
		reval.add(IslandMotOperator.class);
		reval.add(IslandActionOperator.class);
		reval.add(IslandUpdateBeliefsOperator.class);
		return reval;
	}

	@Override
	public List<Class<? extends BaseTranslator>> getTranslatorImpl() {
		List<Class<? extends BaseTranslator>> reval = new ArrayList<>();
		reval.add(IslandTranslator.class);
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

}
