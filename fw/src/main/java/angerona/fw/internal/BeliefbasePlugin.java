package angerona.fw.internal;

import java.util.List;

import net.xeoh.plugins.base.Plugin;
import angerona.fw.BaseBeliefbase;
import angerona.fw.logic.BaseChangeBeliefs;
import angerona.fw.logic.BaseReasoner;

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
	
	/** @return all the supported revisions defined in this plugin */
	public List<Class<? extends BaseChangeBeliefs>> getSupportedChangeOperations();
}
