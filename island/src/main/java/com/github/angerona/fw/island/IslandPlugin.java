package com.github.angerona.fw.island;

import java.util.List;

import com.github.angerona.fw.AgentComponent;
import com.github.angerona.fw.AngeronaPluginAdapter;
import com.github.angerona.fw.EnvironmentBehavior;
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
		// TODO Auto-generated method stub
		return super.getAgentComponentImpl();
	}

	@Override
	public List<Class<? extends BaseTranslator>> getTranslatorImpl() {
		// TODO Auto-generated method stub
		return super.getTranslatorImpl();
	}

	@Override
	public List<Class<? extends BaseOperator>> getOperators() {
		// TODO Auto-generated method stub
		return super.getOperators();
	}

	@Override
	public List<Class<? extends EnvironmentBehavior>> getEnvironmentBehaviors() {
		// TODO Auto-generated method stub
		return super.getEnvironmentBehaviors();
	}
	
	

}
