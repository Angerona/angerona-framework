package com.github.angerona.fw.island;

import java.util.ArrayList;
import java.util.List;

import com.github.angerona.fw.AgentComponent;
import com.github.angerona.fw.AngeronaPluginAdapter;
import com.github.angerona.fw.EnvironmentBehavior;
import com.github.angerona.fw.island.behavior.DynamicIslandBehavior;
import com.github.angerona.fw.island.behavior.IslandBehavior;
import com.github.angerona.fw.island.beliefbase.IslandTranslator;
import com.github.angerona.fw.island.beliefbase.IslandUpdateBeliefsOperator;
import com.github.angerona.fw.island.components.Area;
import com.github.angerona.fw.island.components.Battery;
import com.github.angerona.fw.island.operators.ExecuteOperator;
import com.github.angerona.fw.island.operators.IslandGenerateOptionsOperator;
import com.github.angerona.fw.island.operators.IslandIntentionUpdateOperator;
import com.github.angerona.fw.island.operators.IslandSubgoalGenerationOperator;
import com.github.angerona.fw.logic.BaseTranslator;
import com.github.angerona.fw.operators.BaseOperator;

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
		operators.add(IslandGenerateOptionsOperator.class);
		operators.add(IslandIntentionUpdateOperator.class);
		operators.add(IslandSubgoalGenerationOperator.class);
		operators.add(ExecuteOperator.class);

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
