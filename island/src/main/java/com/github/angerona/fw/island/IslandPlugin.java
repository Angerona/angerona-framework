package com.github.angerona.fw.island;

import java.util.ArrayList;
import java.util.List;

import com.github.angerona.fw.AgentComponent;
import com.github.angerona.fw.AngeronaPluginAdapter;
import com.github.angerona.fw.EnvironmentBehavior;
import com.github.angerona.fw.island.components.Area;
import com.github.angerona.fw.island.components.Battery;
import com.github.angerona.fw.island.operators.ExecuteOperator;
import com.github.angerona.fw.island.operators.HardCodedSubgoalGenerationOperator;
import com.github.angerona.fw.island.operators.RuleBasedGenerateOptionsOperator;
import com.github.angerona.fw.island.operators.RuleBasedIntentionUpdateOperator;
import com.github.angerona.fw.logic.BaseTranslator;
import com.github.angerona.fw.operators.BaseOperator;

/**
 * 
 * @author Manuel Barbi
 *
 */
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
		// TODO Auto-generated method stub
		return super.getTranslatorImpl();
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
		// TODO Auto-generated method stub
		return super.getEnvironmentBehaviors();
	}

}
