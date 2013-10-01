package com.github.angerona.fw;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.simpleframework.xml.transform.Transform;

import com.github.angerona.fw.logic.BaseChangeBeliefs;
import com.github.angerona.fw.logic.BaseReasoner;
import com.github.angerona.fw.logic.BaseTranslator;
import com.github.angerona.fw.operators.BaseOperator;
import com.github.angerona.fw.operators.ContinuousBeliefOperatorFamilyIteratorStrategy;
import com.github.angerona.fw.serialize.SerializeHelper;

/**
 * This class implements empty methods for the AngeronaPlugin interface
 * and is a helper class which allows inherited classes to only define
 * the methods they need. 
 * 
 * @author Tim Janus
 */
public class AngeronaPluginAdapter implements AngeronaPlugin {

	private Map<Class<?>, Class<? extends Transform<?>>> matcherMap = new HashMap<>();
	
	protected void addTransformMapping(Class<?> dataCls, Class<? extends Transform<?>> transformCls) {
		SerializeHelper.get().addTransformMapping(dataCls, transformCls);
		matcherMap.put(dataCls, transformCls);
	}
	
	@Override
	public void onLoading() {}
	
	@Override
	public void unUnloaded() {
		SerializeHelper sh = SerializeHelper.get();
		for(Entry<Class<?>, Class<? extends Transform<?>>> entry : matcherMap.entrySet()) {
			sh.removeTransformMapping(entry.getKey());
		}
		matcherMap.clear();
	}
	
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
