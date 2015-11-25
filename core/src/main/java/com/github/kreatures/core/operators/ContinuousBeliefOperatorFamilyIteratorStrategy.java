package com.github.kreatures.core.operators;

import com.github.kreatures.core.KReaturesPlugin;
import com.github.kreatures.core.serialize.ContinuousBeliefOperatorFamilyConfig;

/**
 * Abstract base class for iterator strategies for the continuous belief-operator-families.
 * Can be extended by the KReatures plugin facilility, see {@link KReaturesPlugin}.
 * 
 * @author Tim Janus
 */
public abstract class ContinuousBeliefOperatorFamilyIteratorStrategy implements BeliefOperatorFamilyIterator {
	protected ContinuousBeliefOperatorFamilyConfig config;
	
	void setConfig(ContinuousBeliefOperatorFamilyConfig config) {
		this.config = config;
	}
}
