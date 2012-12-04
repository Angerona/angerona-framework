package angerona.fw.operators.parameter;

import angerona.fw.BaseBeliefbase;
import angerona.fw.error.ConversionException;
import angerona.fw.operators.GenericOperatorParameter;

public class BeliefbasePluginParameter implements OperatorParameter {
	private BaseBeliefbase owner;

	public BeliefbasePluginParameter() {}
	
	public BeliefbasePluginParameter(BaseBeliefbase owner) {
		this.owner = owner;
	}
	
	public BaseBeliefbase getBeliefBase() {
		return owner;
	}
	
	@Override
	public void fromGenericParameter(GenericOperatorParameter input)
			throws ConversionException {
		if(! (input.getOwner() instanceof BaseBeliefbase) ) {
			throw new ConversionException(GenericOperatorParameter.class, this.getClass(),
					new ClassCastException("Cannot cast owner to base beliefbase."));
		}
		this.owner = (BaseBeliefbase)input.getOwner();
	}
}
