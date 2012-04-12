package angerona.fw;

import java.util.LinkedList;
import java.util.List;

import angerona.fw.logic.ConfidentialKnowledge;

import net.xeoh.plugins.base.annotations.PluginImplementation;

@PluginImplementation
public class DefaultAgentPlugin implements AgentPlugin {

	@Override
	public List<Class<? extends AgentComponent>> getAgentComponents() {
		List<Class<? extends AgentComponent>> reval = new LinkedList<Class<? extends AgentComponent>>();
		reval.add(ConfidentialKnowledge.class);
		return reval;
	}

}
