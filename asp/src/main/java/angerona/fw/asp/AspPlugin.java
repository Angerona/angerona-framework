package angerona.fw.asp;

import java.util.LinkedList;
import java.util.List;

import net.xeoh.plugins.base.annotations.PluginImplementation;

import angerona.fw.BeliefbasePlugin;
import angerona.fw.logic.asp.AspBeliefbase;
import angerona.fw.logic.asp.AspExpansion;
import angerona.fw.logic.asp.AspReasoner;
import angerona.fw.logic.base.BaseBeliefbase;
import angerona.fw.logic.base.BaseConsolidation;
import angerona.fw.logic.base.BaseExpansion;
import angerona.fw.logic.base.BaseReasoner;
import angerona.fw.logic.base.BaseRevision;

@PluginImplementation
public class AspPlugin implements BeliefbasePlugin {

	@Override
	public List<Class<? extends BaseBeliefbase>> getSupportedBeliefbases() {
		List<Class<? extends BaseBeliefbase>> reval = new LinkedList<Class<? extends BaseBeliefbase>>();
		reval.add(AspBeliefbase.class);
		return reval;
	}

	@Override
	public List<Class<? extends BaseReasoner>> getSupportedReasoners() {
		List<Class<? extends BaseReasoner>> reval = new LinkedList<Class<? extends BaseReasoner>>();
		reval.add(AspReasoner.class);
		return reval;
	}

	@Override
	public List<Class<? extends BaseExpansion>> getSupportedExpansionOperations() {
		List<Class<? extends BaseExpansion>> reval = new LinkedList<Class<? extends BaseExpansion>>();
		reval.add(AspExpansion.class);
		return reval;
	}

	@Override
	public List<Class<? extends BaseRevision>> getSupportedRevisionOperations() {
		List<Class<? extends BaseRevision>> reval = new LinkedList<Class<? extends BaseRevision>>();
		return reval;
	}

	@Override
	public List<Class<? extends BaseConsolidation>> getSupportedConsolidationOperations() {
		List<Class<? extends BaseConsolidation>> reval = new LinkedList<Class<? extends BaseConsolidation>>();
		return reval;
	}

}
