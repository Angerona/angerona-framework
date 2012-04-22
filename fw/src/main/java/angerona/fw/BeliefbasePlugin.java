package angerona.fw;

import java.util.List;

import net.xeoh.plugins.base.Plugin;
import angerona.fw.logic.base.BaseBeliefbase;
import angerona.fw.logic.base.BaseConsolidation;
import angerona.fw.logic.base.BaseExpansion;
import angerona.fw.logic.base.BaseReasoner;
import angerona.fw.logic.base.BaseChangeBeliefs;

/**
 * Interface for plugins defining new belief base types.
 * The task of this plugin is to provide different beliefbases (languages) and their
 * basic update operations. So the knowledge representation is fully decoupled from
 * the rest of the framework.
 * @author Tim Janus
 */
public interface BeliefbasePlugin extends Plugin {
	/** @return all the supported belief bases defined in this plugin */
	public List<Class<? extends BaseBeliefbase>> getSupportedBeliefbases();
	
	/** @return all the supported reasoners defined in this plugin */
	public List<Class<? extends BaseReasoner>> getSupportedReasoners();
	
	/** @return all the supported expansions defined in this plugin */
	public List<Class<? extends BaseExpansion>> getSupportedExpansionOperations();
	
	/** @return all the supported revisions defined in this plugin */
	public List<Class<? extends BaseChangeBeliefs>> getSupportedRevisionOperations();
	
	/** @return all the supported consolidations defined in this plugin */
	public List<Class<? extends BaseConsolidation>> getSupportedConsolidationOperations();
}
