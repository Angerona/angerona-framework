package angerona.fw.operators;

import angerona.fw.AngeronaPlugin;
import angerona.fw.serialize.ContinuousBeliefOperatorFamilyConfig;

/**
 * Abstract base class for iterator strategies for the continuous belief-operator-families.
 * Can be extended by the Angerona plugin facilility, see {@link AngeronaPlugin}.
 * 
 * @author Tim Janus
 */
public abstract class ContinuousBeliefOperatorFamilyIteratorStrategy implements BeliefOperatorFamilyIterator {
	protected ContinuousBeliefOperatorFamilyConfig config;
	
	void setConfig(ContinuousBeliefOperatorFamilyConfig config) {
		this.config = config;
	}
}
