package com.github.angerona.fw.serialize;

import java.util.Collections;
import java.util.List;

import org.simpleframework.xml.ElementList;

/**
 * This class is responsible to de/serialize the configuration for discrete
 * belief-operator-families. It contains a list of full java class names that
 * define the order of the operators of the family.
 * 
 * To create a DiscreteBeliefOperatorFamily the factory BeliefOperatorFamilyFactory 
 * is used.
 * @author Tim Janus
 */
public class DiscreteBeliefFamilyConfig extends BeliefOperatorFamilyConfig {
	@ElementList(entry="operator", inline=true)
	private List<String> operatorClasses;
	
	public List<String> getOperatorClasses() {
		return Collections.unmodifiableList(operatorClasses);
	}
}
