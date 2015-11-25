package com.github.kreatures.island;

import java.util.ArrayList;
import java.util.List;

import com.github.kreatures.core.AgentComponent;
import com.github.kreatures.core.KReaturesPluginAdapter;
import com.github.kreatures.core.EnvironmentBehavior;
import com.github.kreatures.island.behavior.DynamicIslandBehavior;
import com.github.kreatures.island.behavior.IslandBehavior;
import com.github.kreatures.island.beliefbase.IslandTranslator;
import com.github.kreatures.island.beliefbase.IslandUpdateBeliefsOperator;
import com.github.kreatures.island.components.Area;
import com.github.kreatures.island.components.Battery;
import com.github.kreatures.island.operators.IslandOptionsOperator;
import com.github.kreatures.island.operators.IslandFilterOperator;
import com.github.kreatures.island.operators.IslandPlanningOperator;
import com.github.kreatures.core.logic.BaseTranslator;
import com.github.kreatures.core.operators.BaseOperator;

import net.xeoh.plugins.base.annotations.PluginImplementation;

/**
 * 
 * @author Manuel Barbi
 *
 */
@PluginImplementation
public class IslandPlugin extends KReaturesPluginAdapter {

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
