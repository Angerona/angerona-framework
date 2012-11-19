package angerona.fw.def;

import java.util.LinkedList;
import java.util.List;

import angerona.fw.EnvironmentBehavior;
import angerona.fw.Perception;
import angerona.fw.internal.SimulationPlugin;
import net.xeoh.plugins.base.annotations.PluginImplementation;

@PluginImplementation
/**
 * The default simulation plugin adds the implementations details for
 * the default simulation behavior.
 * 
 * @see DefaultBeavior
 * @author Tim Janus
 */
public class DefaultSimulationPlugin implements SimulationPlugin {

	@Override
	public List<Class<? extends EnvironmentBehavior>> getEnvironmentBehaviors() {
		List<Class<? extends EnvironmentBehavior>> reval = new LinkedList<>();
		reval.add(DefaultBehavior.class);
		return reval;
	}

	@Override
	public List<Class<? extends Perception>> getPerceptions() {
		return new LinkedList<>();
	}
	
}
