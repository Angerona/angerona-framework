package angerona.fw.internal;

import java.util.List;

import net.xeoh.plugins.base.Plugin;
import angerona.fw.operators.BaseOperator;

/**
 * Interface for plugins defining new operator functionality.
 * The operators provide by this plugin change the functionality of the 
 * Angerona framework.
 * For defining new logical languages and so on see the BeliefbasePlugin.
 * @see angerona.fw.internal.BeliefbasePlugin
 * @author Tim Janus
 */
public interface OperatorPlugin extends Plugin {

	/** @return all registered operators defined in this plugin */
	public List<Class<? extends BaseOperator>> getOperators();
}
