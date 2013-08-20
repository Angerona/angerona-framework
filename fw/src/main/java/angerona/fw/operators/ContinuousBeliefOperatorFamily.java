package angerona.fw.operators;

import angerona.fw.AngeronaPlugin;
import angerona.fw.OperatorSet;
import angerona.fw.internal.PluginInstantiator;
import angerona.fw.serialize.ContinuousBeliefOperatorFamilyConfig;

/**
 * This class implements a continuous belief-operator-family, that means the operator
 * family does not uses multiple class implementations but a parameter that is interpreted
 * as a range like [0, 1]. 
 * 
 * The user can provide different iterator implementations by giving the full-java-class
 * name of a sub class of {@link ContinuousBeliefOperatorFamilyIteratorStrategy}. In the default
 * case {@link StepIteratorStrategy} is used. 
 * 
 * New iterator strategies can be implemented using the Angerona Plug-in facility, see
 * {@link AngeronaPlugin}.
 * 
 * @author Tim Janus
 *
 */
public class ContinuousBeliefOperatorFamily implements BeliefOperatorFamily {
	
	/** the implementation of the operator used by the implementation */
	private BaseOperator operator;
	
	/** the loaded configuration for this belief-operator-family */
	private ContinuousBeliefOperatorFamilyConfig config;
	
	/** the used iterator implementation */
	private ContinuousBeliefOperatorFamilyIteratorStrategy iteratorImplementation;
	
	public ContinuousBeliefOperatorFamily(ContinuousBeliefOperatorFamilyConfig config, OperatorSet opSet) 
		throws java.lang.InstantiationException, IllegalAccessException {
		this.config = config;
		OperatorCallWrapper ocw = opSet.getOperator(config.getOperatorCls());
		if(ocw == null)
			throw new InstantiationException("Cannot find reasoner '" + config.getOperatorCls() + "'.");
		operator = ocw.getImplementation();
		String iteratorCls = this.config.getIteratorCls();
		iteratorImplementation = PluginInstantiator.getInstance().createIteratorStrategy(iteratorCls);
		iteratorImplementation.setConfig(this.config);
	}
	
	@Override
	public OperatorCallWrapper getPredecessor(OperatorCallWrapper current) {
		return iteratorImplementation.getPredecessor(current);
	}

	@Override
	public OperatorCallWrapper getSuccessor(OperatorCallWrapper current) {
		return iteratorImplementation.getSuccessor(current);
	}

	@Override
	public boolean addOperator(OperatorCallWrapper toAdd,
			OperatorCallWrapper predecessor) {
		throw new IllegalStateException();
	}
}
