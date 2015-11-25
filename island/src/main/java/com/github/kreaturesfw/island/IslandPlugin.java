package com.github.kreaturesfw.island;

import java.util.ArrayList;
import java.util.List;

import com.github.kreaturesfw.core.AgentComponent;
import com.github.kreaturesfw.core.AngeronaPluginAdapter;
import com.github.kreaturesfw.core.EnvironmentBehavior;
import com.github.kreaturesfw.core.logic.BaseTranslator;
import com.github.kreaturesfw.core.operators.BaseOperator;
import com.github.kreaturesfw.island.behavior.DynamicIslandBehavior;
import com.github.kreaturesfw.island.behavior.IslandBehavior;
import com.github.kreaturesfw.island.beliefbase.IslandTranslator;
import com.github.kreaturesfw.island.beliefbase.IslandUpdateBeliefsOperator;
import com.github.kreaturesfw.island.components.Area;
import com.github.kreaturesfw.island.components.Battery;
import com.github.kreaturesfw.island.operators.IslandFilterOperator;
import com.github.kreaturesfw.island.operators.IslandOptionsOperator;
import com.github.kreaturesfw.island.operators.IslandPlanningOperator;

import net.xeoh.plugins.base.annotations.PluginImplementation;

/**
 * 
 * @author Manuel Barbi
 *
 */
@PluginImplementation
public class IslandPlugin extends AngeronaPluginAdapter {

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
		operators.add(IslandOptionsOperator.class);
		operators.add(IslandFilterOperator.class);
		operators.add(IslandPlanningOperator.class);

		operators.add(IslandUpdateBeliefsOperator.class);
		return operators;
	}

	@Override
	public List<Class<? extends EnvironmentBehavior>> getEnvironmentBehaviors() {
		List<Class<? extends EnvironmentBehavior>> behaviors = new ArrayList<>();
		behaviors.add(IslandBehavior.class);
		behaviors.add(DynamicIslandBehavior.class);
		return behaviors;
	}

}
