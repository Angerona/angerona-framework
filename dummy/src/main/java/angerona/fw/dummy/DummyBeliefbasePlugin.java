package angerona.fw.dummy;

import java.util.LinkedList;
import java.util.List;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import angerona.fw.BeliefbasePlugin;
import angerona.fw.logic.base.BaseBeliefbase;
import angerona.fw.logic.base.BaseConsolidation;
import angerona.fw.logic.base.BaseExpansion;
import angerona.fw.logic.base.BaseReasoner;
import angerona.fw.logic.base.BaseChangeBeliefs;
import angerona.fw.logic.dummy.DummyBeliefbase;
import angerona.fw.logic.dummy.DummyConsolidation;
import angerona.fw.logic.dummy.DummyExpansion;
import angerona.fw.logic.dummy.DummyReasoner;
import angerona.fw.logic.dummy.DummyRevision;

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
	public List<Class<? extends BaseExpansion>> getSupportedExpansionOperations() {
		List<Class<? extends BaseExpansion>> reval = new LinkedList<Class<? extends BaseExpansion>>();
		reval.add(DummyExpansion.class);
		return reval;
	}

	@Override
	public List<Class<? extends BaseChangeBeliefs>> getSupportedRevisionOperations() {
		List<Class<? extends BaseChangeBeliefs>> reval = new LinkedList<Class<? extends BaseChangeBeliefs>>();
		reval.add(DummyRevision.class);
		return reval;
	}

	@Override
	public List<Class<? extends BaseConsolidation>> getSupportedConsolidationOperations() {
		List<Class<? extends BaseConsolidation>> reval = new LinkedList<Class<? extends BaseConsolidation>>();
		reval.add(DummyConsolidation.class);
		return reval;
	}

}
