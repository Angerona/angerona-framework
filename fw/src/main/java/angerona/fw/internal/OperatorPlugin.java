package angerona.fw.internal;

import java.util.List;

import net.xeoh.plugins.base.Plugin;
import angerona.fw.operators.BaseGenerateOptionsOperator;
import angerona.fw.operators.BaseIntentionUpdateOperator;
import angerona.fw.operators.BaseSubgoalGenerationOperator;
import angerona.fw.operators.BaseUpdateBeliefsOperator;
import angerona.fw.operators.BaseViolatesOperator;

/**
 * Interface for plugins defining new operator functionality.
 * The operators provide by this plugin change the functionality of the 
 * Angerona framework.
 * For defining new logical languages and so on see the BeliefbasePlugin.
 * @see angerona.fw.internal.BeliefbasePlugin
 * @author Tim Janus
 */
public interface OperatorPlugin extends Plugin {
	
	/** @return all the supported Update Operators defined in this plugin */
	public List<Class<? extends BaseUpdateBeliefsOperator>> getSupportedChangeOperators();
	
	/** @return all the supported filter Operators defined in this plugin */
	public List<Class<? extends BaseIntentionUpdateOperator>> getSupportedFilterOperators();
	
	/** @return all the supported Generate-Options Operators defined in this plugin */
	public List<Class<? extends BaseGenerateOptionsOperator>> getSupportedGenerateOptionsOperators();
	
	/** @return all the supported Update Operators defined in this plugin */
	public List<Class<? extends BaseViolatesOperator>> getSupportedViolatesOperators();
	
	/** @return all the supported Planer Operators defined in this plugin */
	public List<Class<? extends BaseSubgoalGenerationOperator>> getSupportedPlaners();
}
