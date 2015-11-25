package com.github.kreatures.core.operators.parameter;

import javax.management.AttributeNotFoundException;

import com.github.kreatures.core.BaseBeliefbase;
import com.github.kreatures.core.error.ConversionException;
import com.github.kreatures.core.operators.OperatorStack;
import com.github.kreatures.core.report.Reporter;

/**
 * Base class for input parameter of operators of the belief base plugin. 
 * At this point in the hierarchy it is obvious that the caller is a 
 * belief base. Therefore the class implements access methods to receive
 * the caller as belief base and implements a basic version of the
 * fromGenericParameter method which reads the caller from the generic 
 * parameter.
 * 
 * @author Tim Janus
 */
public class BeliefbasePluginParameter extends OperatorParameterAdapter {
	/** the caller of the operator */
	private BaseBeliefbase caller;

	/** Default Ctor: used for dynamic instatiation */
	public BeliefbasePluginParameter() {}
	
	/** Ctor: Used to generate parameter structures in code */
	public BeliefbasePluginParameter(BaseBeliefbase owner) {
		this.caller = owner;
	}
	
	/** @return the belief base instance which is the caller of the operator */
	public BaseBeliefbase getBeliefBase() {
		return caller;
	}
	
	/**
	 * Reads the caller from the generic parameter. The caller is cast to a BaseBeliefbase
	 * instance and saved in this object. Subclasses shall override this method to request more
	 * parameters from the generic parameter structure. The override method has to call the super
	 * method.
	 * @param input	The input parameters in generic form.
	 * @throws AttributeNotFoundException 
	 */
	@Override
	public void fromGenericParameter(GenericOperatorParameter input)
			throws ConversionException, AttributeNotFoundException {
		super.fromGenericParameter(input);
		
		if(! (input.getCaller() instanceof BaseBeliefbase) ) {
			throw new ConversionException(GenericOperatorParameter.class, this.getClass(),
					new ClassCastException("Cannot cast owner to base beliefbase."));
		}
		this.caller = (BaseBeliefbase)input.getCaller();
	}

	/**
	 * Returns the reporter which shall be used by functional operators, the belief
	 * base as a sub class of base agent component has an reporter itself but this
	 * reporter is statically bound to the base belief base. Therefore the agents
	 * reporter is used. When the operator call stack is updated then the report poster
	 * of the agent's reporter is also updated.
	 * @return	An interface to report system which dynamically keeps track of the correct
	 * 			report poster.
	 */
	@Override
	public Reporter getReporter() {
		return caller.getReporter();
	}

	/**
	 * Use the stack provided by the agent.
	 */
	@Override
	public OperatorStack getStack() {
		return caller.getStack();
	}
}
