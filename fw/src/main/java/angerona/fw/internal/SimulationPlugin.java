package angerona.fw.internal;

import java.util.List;

import net.xeoh.plugins.base.Plugin;
import angerona.fw.EnvironmentBehavior;

/**
 * 	A Simulation Plugin can adapt the handling of simulations by providing
 *  new environment behaviors which allow to define communication between 
 *  the environment and simulations. The plugin can also register new perceptions, but
 *  this perception should be perceptions only and no actions because actions are
 *  registered using the agents plugin.
 *  
 *  @see EnvironmentBehavior
 * 	@author Tim Janus
 */
public interface SimulationPlugin extends Plugin {
	List<Class<? extends EnvironmentBehavior>> getEnvironmentBehaviors();
}
