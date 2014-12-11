package com.github.angerona.fw.island;

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
import com.github.angerona.fw.island.comp.Area;
import com.github.angerona.fw.island.comp.Battery;
import com.github.angerona.fw.motivation.behavior.BasicBehavior;
import com.github.angerona.fw.motivation.dao.impl.LevelWeights;
import com.github.angerona.fw.motivation.dao.impl.MotStructure;
import com.github.angerona.fw.motivation.dao.impl.MotiveCouplings;
import com.github.angerona.fw.motivation.dao.impl.WeightRanges;
import com.github.angerona.fw.motivation.operators.MotivationOperator;
import com.github.angerona.fw.motivation.reliable.impl.ActionSequences;
import com.github.angerona.fw.motivation.reliable.impl.TimeSlots;
import com.github.angerona.fw.motivation.view.CouplingsView;
import com.github.angerona.fw.motivation.view.MotStructureView;
import com.github.angerona.fw.motivation.view.RangesView;
import com.github.angerona.fw.motivation.view.WeightsView;
import com.github.angerona.fw.operators.BaseOperator;

/**
 * {@link IslandPlugin} introduces the basic operator to integrate the motivation component into the framework. as well as the depending
 * {@link AgentComponent}s and {@link ViewComponent}s.
 * 
 * @author Manuel Barbi
 * 
 */
@PluginImplementation
public class IslandPlugin extends AngeronaPluginAdapter implements UIPlugin {

	@Override
	public List<Class<? extends AgentComponent>> getAgentComponentImpl() {
		List<Class<? extends AgentComponent>> reval = new ArrayList<>();
		reval.add(Area.class);
		reval.add(Battery.class);
		return reval;
	}

	@Override
	public Map<String, Class<? extends ViewComponent>> getUIComponents() {
		Map<String, Class<? extends ViewComponent>> reval = new HashMap<String, Class<? extends ViewComponent>>();
		return reval;
	}

	@Override
	public List<Class<? extends EnvironmentBehavior>> getEnvironmentBehaviors() {
		List<Class<? extends EnvironmentBehavior>> reval = new ArrayList<>();
		reval.add(StaticIslandBehavior.class);
		reval.add(DynamicIslandBehavior.class);
		return reval;
	}

}
