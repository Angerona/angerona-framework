package angerona.fw.internal;

import java.util.List;

import net.xeoh.plugins.base.Plugin;
import angerona.fw.AgentComponent;
import angerona.fw.BaseAgentComponent;

/**
 * An Agent plugin provides custom AgentComponents. An 
 * AgentComponent is an implementation of the agent data component
 * like the ConfidentialKnowledge or Know-how.
 * 
 * @see BaseAgentComponent
 * @author Tim Janus
 */
public interface AgentPlugin extends Plugin{
	List<Class<? extends AgentComponent>> getAgentComponents();
}
