package angerona.fw.dummy;

import java.util.LinkedList;
import java.util.List;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import angerona.fw.BaseBeliefbase;
import angerona.fw.internal.BeliefbasePlugin;
import angerona.fw.logic.BaseChangeBeliefs;
import angerona.fw.logic.BaseReasoner;
import angerona.fw.logic.dummy.DummyBeliefbase;
import angerona.fw.logic.dummy.DummyExpansion;
import angerona.fw.logic.dummy.DummyReasoner;

/**
 * A dummy belief base plugin. It is used as an example and for testing purposes.
 * It has to work with the Angerona framework. And should show how to decouple the
 * knowledge representation from the rest of the framework.
 * @author Tim Janus
 */
@PluginImplementation
public class DummyBeliefbasePlugin implements BeliefbasePlugin{

	@Override
	public List<Class<? extends BaseBeliefbase>> getSupportedBeliefbases() {
		List<Class<? extends BaseBeliefbase>> reval = new LinkedList<Class<? extends BaseBeliefbase>>();
		reval.add(DummyBeliefbase.class);
		return reval;
	}

	@Override
	public List<Class<? extends BaseReasoner>> getSupportedReasoners() {
		List<Class<? extends BaseReasoner>> reval = new LinkedList<Class<? extends BaseReasoner>>();
		reval.add(DummyReasoner.class);
		return reval;
	}

	@Override
	public List<Class<? extends BaseChangeBeliefs>> getSupportedChangeOperations() {
		List<Class<? extends BaseChangeBeliefs>> reval = new LinkedList<Class<? extends BaseChangeBeliefs>>();
		reval.add(DummyExpansion.class);
		return reval;
	}
}
