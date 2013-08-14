package angerona.fw;

import java.util.List;

import net.xeoh.plugins.base.Plugin;
import angerona.fw.logic.BaseChangeBeliefs;
import angerona.fw.logic.BaseReasoner;
import angerona.fw.logic.BaseTranslator;
import angerona.fw.operators.BaseOperator;
import angerona.fw.operators.ContinuousBeliefOperatorFamilyIteratorStrategy;


/**
 * This interface represents an Angerona plugin. It provides list of class
 * implementations for different purposes. One can add new agent data components
 * to the agent. 
 * It is also possible to define new types of belief bases and their
 * operators like reasoner, change and translator to implement new knowledge
 * representation methods which are fully decoupled from the agent interface 
 * allowing an agent to use different belief bases for world and views on other
 * agents and so on.
 * By providing operator implementations it is possible to change the agent cycle
 * to support different agent models. For further details see the SecrecyPlugin.
 * It is possible to define environment behavior implementations which decide
 * when the local agents are updated, if external simulation software is invoked
 * and so on.
 * 
 * @author Tim Janus
 */
public interface AngeronaPlugin extends Plugin {
	/** @return the agent component implementations defined in the plugin */
	List<Class<? extends AgentComponent>> getAgentComponentImpl();
	
	/** @return all the belief base implementations defined in the plugin */
	List<Class<? extends BaseBeliefbase>> getBeliefbaseImpl();
	
	/** @return all the reasoner implementations defined in the plugin */
	List<Class<? extends BaseReasoner>> getReasonerImpl();
	
	/** @return all the implementations of change operations on belief bases defined in the plugin */
	List<Class<? extends BaseChangeBeliefs>> getChangeImpl();
	
	/** @return all the translator implementations defined in the plugin */
	List<Class<? extends BaseTranslator>> getTranslatorImpl();
	
	/** @return all operator implementations defined in this plugin */
	List<Class<? extends BaseOperator>> getOperators();
	
	/** @return all the environment behavior implementations of the plugin */
	List<Class<? extends EnvironmentBehavior>> getEnvironmentBehaviors();
	
	/** @return all the iterator strategies, that can be used to iterate over continuous belief operator families */
	List<Class<? extends ContinuousBeliefOperatorFamilyIteratorStrategy>> getBeliefOperatorFamilyIteratorStrategies();
}
