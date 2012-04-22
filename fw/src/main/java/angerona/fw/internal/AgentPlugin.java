package angerona.fw.internal;

import java.util.List;

import angerona.fw.AgentComponent;

import net.xeoh.plugins.base.Plugin;

/**
 * This is a plugin 
 * @author Tim Janus
 */
public interface AgentPlugin extends Plugin{
	List<Class<? extends AgentComponent>> getAgentComponents();
}
