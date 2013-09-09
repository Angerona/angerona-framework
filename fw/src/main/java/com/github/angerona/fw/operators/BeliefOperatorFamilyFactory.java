package com.github.angerona.fw.operators;

import com.github.angerona.fw.serialize.BeliefOperatorFamilyConfig;
import com.github.angerona.fw.serialize.ContinuousBeliefOperatorFamilyConfig;
import com.github.angerona.fw.serialize.DiscreteBeliefFamilyConfig;

/**
 * This class represents a factory that can generate belief-operator-families with their
 * given XML configuration and an operator set that acts as basis of the belief-operator-family,
 * therefore the method create() is used.
 * 
 * @author Tim Janus
 */
public class BeliefOperatorFamilyFactory {
	
	/**
	 * Create a belief operator family, the type depends on the given belief-operator-family configuration.
	 * @param config	The belief-operator-family configuration
	 * @param opSet		A operation set that is used as basis for the belief-operator-family.
	 * @return
	 * @throws InstantiationException
	 */
	public static BeliefOperatorFamily create(BeliefOperatorFamilyConfig config, OperatorSet opSet) throws InstantiationException {
		if(config instanceof DiscreteBeliefFamilyConfig) {
			return createDiscrete((DiscreteBeliefFamilyConfig)config, opSet);
		} else if(config instanceof ContinuousBeliefOperatorFamilyConfig) {
			return createContiniuous((ContinuousBeliefOperatorFamilyConfig)config, opSet);
		}
	
		return null;
	}

	private static BeliefOperatorFamily createContiniuous(ContinuousBeliefOperatorFamilyConfig config, OperatorSet opSet) 
			throws InstantiationException {
		try {
			return new ContinuousBeliefOperatorFamily(config, opSet);
		} catch(IllegalAccessException|InstantiationException e) {
			throw new InstantiationException(e.getMessage());
		}
	}
	
	/**
	 * Creates a discrete belief operator family, that means the family is fully defined by giving an ordered
	 * set of belief-operators (reasoners) in the config parameter.
	 * @param config	The configuration of the discrete belief-operator-family.
	 * @param opSet		The operation set that acts as basis for the belief-operator-family.
	 * @return
	 * @throws InstantiationException
	 */
	private static BeliefOperatorFamily createDiscrete(
			DiscreteBeliefFamilyConfig config, OperatorSet opSet)
			throws InstantiationException {
		DiscreteBeliefOperatorFamily reval = new DiscreteBeliefOperatorFamily();
		
		for(String clsName : config.getOperatorClasses()) {
			OperatorCallWrapper ocw = opSet.getOperator(clsName);
			if(ocw != null) {
				reval.addOperator(ocw, null);
			} else {
				throw new InstantiationException("Cannot instantiate belief-operator-family with: '" + clsName + "')");
			}
		}
		return reval;
	}
}
