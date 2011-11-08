package angerona.fw;

import java.util.List;

import angerona.fw.operators.BaseIntentionUpdateOperator;
import angerona.fw.operators.BaseGenerateOptionsOperator;
import angerona.fw.operators.BasePolicyControlOperator;
import angerona.fw.operators.BaseChangeOperator;
import angerona.fw.operators.BaseViolatesOperator;
import angerona.fw.operators.BaseSubgoalGenerationOperator;

import net.xeoh.plugins.base.Plugin;

/**
 * Interface for plugins defining new operator functionality.
 * The operators provide by this plugin change the functionality of the 
 * Angerona framework.
 * For defining new logical languages and so on see the BeliefbasePlugin.
 * @see angerona.fw.BeliefbasePlugin
 * @author Tim Janus
 */
public interface OperatorPlugin extends Plugin {
	
	/** @return all the supported Update Operators defined in this plugin */
	public List<Class<? extends BaseChangeOperator>> getSupportedChangeOperators();
	
	/** @return all the supported filter Operators defined in this plugin */
	public List<Class<? extends BaseIntentionUpdateOperator>> getSupportedFilterOperators();
	
	/** @return all the supported Generate-Options Operators defined in this plugin */
	public List<Class<? extends BaseGenerateOptionsOperator>> getSupportedGenerateOptionsOperators();
	
	/** @return all the supported Policy-Control Operators defined in this plugin */
	public List<Class<? extends BasePolicyControlOperator>> getSupportedPolicyControlOperators();
	
	/** @return all the supported Update Operators defined in this plugin */
	public List<Class<? extends BaseViolatesOperator>> getSupportedViolatesOperators();
	
	/** @return all the supported Planer Operators defined in this plugin */
	public List<Class<? extends BaseSubgoalGenerationOperator>> getSupportedPlaners();
}
