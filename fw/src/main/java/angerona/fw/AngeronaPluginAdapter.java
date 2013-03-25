package angerona.fw;

import java.util.LinkedList;
import java.util.List;

import angerona.fw.logic.BaseChangeBeliefs;
import angerona.fw.logic.BaseReasoner;
import angerona.fw.logic.BaseTranslator;
import angerona.fw.operators.BaseOperator;

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

}
