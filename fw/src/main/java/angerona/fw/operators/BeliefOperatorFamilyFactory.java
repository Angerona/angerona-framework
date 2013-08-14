package angerona.fw.operators;

import angerona.fw.OperatorSet;
import angerona.fw.serialize.BeliefOperatorFamilyConfig;
import angerona.fw.serialize.DiscreteBeliefFamilyConfig;

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
		}
	
		return null;
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
