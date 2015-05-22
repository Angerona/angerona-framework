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
import com.github.angerona.fw.island.behavior.DynamicIslandBehavior;
import com.github.angerona.fw.island.behavior.IslandBehavior;
import com.github.angerona.fw.island.beliefbase.IslandTranslator;
import com.github.angerona.fw.island.components.Area;
import com.github.angerona.fw.island.components.Battery;
import com.github.angerona.fw.island.operators.ExecuteOperator;
import com.github.angerona.fw.island.operators.HardCodedSubgoalGenerationOperator;
import com.github.angerona.fw.island.operators.RuleBasedGenerateOptionsOperator;
import com.github.angerona.fw.island.operators.RuleBasedIntentionUpdateOperator;
import com.github.angerona.fw.island.view.AreaView;
import com.github.angerona.fw.island.view.BatteryView;
import com.github.angerona.fw.logic.BaseTranslator;
import com.github.angerona.fw.operators.BaseOperator;

/**
 * 
 * @author Manuel Barbi
 *
 */
@PluginImplementation
public class IslandPlugin extends AngeronaPluginAdapter implements UIPlugin {

	@Override
	public List<Class<? extends AgentComponent>> getAgentComponentImpl() {
		List<Class<? extends AgentComponent>> components = new ArrayList<>();
		components.add(Area.class);
		components.add(Battery.class);
		return components;
	}

	@Override
	public List<Class<? extends BaseTranslator>> getTranslatorImpl() {
		List<Class<? extends BaseTranslator>> translators = new ArrayList<>();
		translators.add(IslandTranslator.class);
		return translators;
	}

	@Override
	public List<Class<? extends BaseOperator>> getOperators() {
		List<Class<? extends BaseOperator>> operators = new ArrayList<>();
		operators.add(RuleBasedGenerateOptionsOperator.class);
		operators.add(RuleBasedIntentionUpdateOperator.class);
		operators.add(HardCodedSubgoalGenerationOperator.class);
		operators.add(ExecuteOperator.class);
		return operators;
	}

	@Override
	public List<Class<? extends EnvironmentBehavior>> getEnvironmentBehaviors() {
		List<Class<? extends EnvironmentBehavior>> behaviors = new ArrayList<>();
		behaviors.add(IslandBehavior.class);
		behaviors.add(DynamicIslandBehavior.class);
		return behaviors;
	}

	@Override
	public Map<String, Class<? extends ViewComponent>> getUIComponents() {
		Map<String, Class<? extends ViewComponent>> views = new HashMap<>();
		views.put("AreaView", AreaView.class);
		views.put("BatteryView", BatteryView.class);
		return views;
	}

}
