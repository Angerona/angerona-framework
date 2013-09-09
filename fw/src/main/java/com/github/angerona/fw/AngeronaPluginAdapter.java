package com.github.angerona.fw;

import java.util.LinkedList;
import java.util.List;

import com.github.angerona.fw.logic.BaseChangeBeliefs;
import com.github.angerona.fw.logic.BaseReasoner;
import com.github.angerona.fw.logic.BaseTranslator;
import com.github.angerona.fw.operators.BaseOperator;
import com.github.angerona.fw.operators.ContinuousBeliefOperatorFamilyIteratorStrategy;

/**
 * This class implements empty methods for the AngeronaPlugin interface
 * and is a helper class which allows inherited classes to only define
 * the methods they need. 
 * 
 * @author Tim Janus
 */
public class AngeronaPluginAdapter implements AngeronaPlugin {

	@Override
	public List<Class<? extends AgentComponent>> getAgentComponentImpl() {
		return new LinkedList<>();
	}

	@Override
	public List<Class<? extends BaseBeliefbase>> getBeliefbaseImpl() {
		return new LinkedList<>();
	}

	@Override
	public List<Class<? extends BaseReasoner>> getReasonerImpl() {
		return new LinkedList<>();
	}

	@Override
	public List<Class<? extends BaseChangeBeliefs>> getChangeImpl() {
		return new LinkedList<>();
	}

	@Override
	public List<Class<? extends BaseTranslator>> getTranslatorImpl() {
		return new LinkedList<>();
	}

	@Override
	public List<Class<? extends BaseOperator>> getOperators() {
		return new LinkedList<>();
	}

	@Override
	public List<Class<? extends EnvironmentBehavior>> getEnvironmentBehaviors() {
		return new LinkedList<>();
	}

	@Override
	public List<Class<? extends ContinuousBeliefOperatorFamilyIteratorStrategy>> getBeliefOperatorFamilyIteratorStrategies() {
		return new LinkedList<>();
	}
}
