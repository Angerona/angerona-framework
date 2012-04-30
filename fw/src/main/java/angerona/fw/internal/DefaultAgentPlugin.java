package angerona.fw.internal;

import java.util.LinkedList;
import java.util.List;

import net.xeoh.plugins.base.annotations.PluginImplementation;
import angerona.fw.AgentComponent;
import angerona.fw.MasterPlan;
import angerona.fw.logic.ConfidentialKnowledge;

@PluginImplementation
/**
 * The default agent plugin for the Angerona framework defines
 * the confidential Knowledge and is part of the main-framework.
 *  
 * @author Tim Janus
 */
public class DefaultAgentPlugin implements AgentPlugin {

	@Override
	public List<Class<? extends AgentComponent>> getAgentComponents() {
		List<Class<? extends AgentComponent>> reval = new LinkedList<Class<? extends AgentComponent>>();
		reval.add(ConfidentialKnowledge.class);
		reval.add(MasterPlan.class);
		return reval;
	}

}
