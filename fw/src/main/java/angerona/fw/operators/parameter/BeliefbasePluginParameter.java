package angerona.fw.operators.parameter;

import angerona.fw.BaseBeliefbase;
import angerona.fw.error.ConversionException;
import angerona.fw.operators.GenericOperatorParameter;

public class BeliefbasePluginParameter implements OperatorParameter {
	private BaseBeliefbase caller;

	public BeliefbasePluginParameter() {}
	
	public BeliefbasePluginParameter(BaseBeliefbase owner) {
		this.caller = owner;
	}
	
	public BaseBeliefbase getBeliefBase() {
		return caller;
	}
	
	@Override
	public void fromGenericParameter(GenericOperatorParameter input)
			throws ConversionException {
		if(! (input.getCaller() instanceof BaseBeliefbase) ) {
			throw new ConversionException(GenericOperatorParameter.class, this.getClass(),
					new ClassCastException("Cannot cast owner to base beliefbase."));
		}
		this.caller = (BaseBeliefbase)input.getCaller();
	}
}
