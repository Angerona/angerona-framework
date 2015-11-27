package com.github.kreaturesfw.core;

import java.util.List;

import com.github.kreaturesfw.core.basic.AgentComponent;
import com.github.kreaturesfw.core.basic.EnvironmentBehavior;
import com.github.kreaturesfw.core.basic.Operator;
import com.github.kreaturesfw.core.bdi.components.BaseBeliefbase;
import com.github.kreaturesfw.core.logic.BaseChangeBeliefs;
import com.github.kreaturesfw.core.logic.BaseReasoner;
import com.github.kreaturesfw.core.logic.BaseTranslator;
import com.github.kreaturesfw.core.operators.ContinuousBeliefOperatorFamilyIteratorStrategy;

import net.xeoh.plugins.base.Plugin;


/**
 * This interface represents an Angerona plug-in. It provides list of class
 * implementations for different purposes. 
 * 
 * New agent data components can be added to the Angerona
 * framework by implementing this interface.
 * It is also possible to define new types of belief bases and their
 * operators like reasoner, change and translator, such that new knowledge
 * representation mechanisms can be implemented. Those mechanisms are 
 * decoupled from the agent interface, such that the agent can use different 
 * belief base types for it's world knowledge and for it's views on other agent's 
 * knowledge.
 * 
 * By providing operator implementations it is possible to change the agent cycle
 * to support different agent models. For further details see the SecrecyPlugin.
 * It is possible to define environment behavior implementations which decide
 * when the local agents are updated and if an external simulation software is invoked.
 * 
 * @author Tim Janus
 */
public interface KReaturesPlugin extends Plugin {
	/** is called right before the plug-in is loaded */
	void onLoading();
	
	/** is called after the plug-in is unloaded */
	void unUnloaded();
	
	/** @return the agent component implementations defined in the plug-in */
	List<Class<? extends AgentComponent>> getAgentComponentImpl();
	
	/** @return all the belief base implementations defined in the plug-in */
	List<Class<? extends BaseBeliefbase>> getBeliefbaseImpl();
	
	/** @return all the reasoner implementations defined in the plug-in */
	List<Class<? extends BaseReasoner>> getReasonerImpl();
	
	/** @return all the implementations of change operations on belief bases defined in the plug-in */
	List<Class<? extends BaseChangeBeliefs>> getChangeImpl();
	
	/** @return all the translator implementations defined in the plug-in */
	List<Class<? extends BaseTranslator>> getTranslatorImpl();
	
	/** @return all operator implementations defined in this plug-in */
	List<Class<? extends Operator>> getOperators();
	
	/** @return all the environment behavior implementations of the plug-in */
	List<Class<? extends EnvironmentBehavior>> getEnvironmentBehaviors();
	
	/** @return all the iterator strategies, that can be used to iterate over continuous belief operator families */
	List<Class<? extends ContinuousBeliefOperatorFamilyIteratorStrategy>> getBeliefOperatorFamilyIteratorStrategies();
}
