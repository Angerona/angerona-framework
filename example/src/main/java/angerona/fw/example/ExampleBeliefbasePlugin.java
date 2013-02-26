package angerona.fw.example;

import java.util.LinkedList;
import java.util.List;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import angerona.fw.BaseBeliefbase;
import angerona.fw.example.logic.ExampleBeliefbase;
import angerona.fw.example.logic.ExampleExpansion;
import angerona.fw.example.logic.ExampleReasoner;
import angerona.fw.example.logic.ExampleTranslator;
import angerona.fw.internal.BeliefbasePlugin;
import angerona.fw.logic.BaseChangeBeliefs;
import angerona.fw.logic.BaseReasoner;
import angerona.fw.logic.BaseTranslator;

/**
 * A dummy belief base plugin. It is used as an example and for testing purposes.
 * It has to work with the Angerona framework. And should show how to decouple the
 * knowledge representation from the rest of the framework.
 * @author Tim Janus
 */
@PluginImplementation
public class ExampleBeliefbasePlugin implements BeliefbasePlugin{

	@Override
	public List<Class<? extends BaseBeliefbase>> getSupportedBeliefbases() {
		List<Class<? extends BaseBeliefbase>> reval = new LinkedList<Class<? extends BaseBeliefbase>>();
		reval.add(ExampleBeliefbase.class);
		return reval;
	}

	@Override
	public List<Class<? extends BaseReasoner>> getSupportedReasoners() {
		List<Class<? extends BaseReasoner>> reval = new LinkedList<Class<? extends BaseReasoner>>();
		reval.add(ExampleReasoner.class);
		return reval;
	}

	@Override
	public List<Class<? extends BaseChangeBeliefs>> getSupportedChangeOperations() {
		List<Class<? extends BaseChangeBeliefs>> reval = new LinkedList<Class<? extends BaseChangeBeliefs>>();
		reval.add(ExampleExpansion.class);
		return reval;
	}

	@Override
	public List<Class<? extends BaseTranslator>> getSupportedTranslators() {
		List<Class<? extends BaseTranslator>> reval = new LinkedList<Class<? extends BaseTranslator>>();
		reval.add(ExampleTranslator.class);
		return reval;
	}
}
