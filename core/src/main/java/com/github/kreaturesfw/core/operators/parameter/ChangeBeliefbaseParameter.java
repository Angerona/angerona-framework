package com.github.kreaturesfw.core.operators.parameter;

import javax.management.AttributeNotFoundException;

import com.github.kreaturesfw.core.BaseBeliefbase;
import com.github.kreaturesfw.core.error.ConversionException;

/**
 * Input-parameter class for change operations on a belief bases.
 * 
 * @author Tim Janus
 */
public class ChangeBeliefbaseParameter extends BeliefbasePluginParameter {
	/** belief base representing the new knowledge. */
	private BaseBeliefbase newKnowledge;
	
	/** Default Ctor: Used for dynamic instantiation */
	public ChangeBeliefbaseParameter() {}
	
	/**
	 * Ctor: Used to generate an instance of the class in-code.
	 * @param source		belief base
	 * @param newKnowledge	set of formula with new knowledge.
	 */
	public ChangeBeliefbaseParameter(BaseBeliefbase source, BaseBeliefbase newKnowledge) {
		super(source);
		this.newKnowledge = newKnowledge;
	}
	
	/**
	 * Calls it super method and receives the newBelief belief base from 
	 * the generic parameter and encapsulates it for further used by the operator.
	 * @throws AttributeNotFoundException 
	 */
	@Override
	public void fromGenericParameter(GenericOperatorParameter input)
			throws ConversionException, AttributeNotFoundException {
		super.fromGenericParameter(input);
		this.newKnowledge = (BaseBeliefbase)input.getParameter("newBelief");
	}

	/** @return the belief base on which the update operation will be performed */
	public BaseBeliefbase getSourceBeliefBase() {
		return super.getBeliefBase();
	}

	/** @return a set of formulas representing the new knowledge */
	public BaseBeliefbase getNewKnowledge() {
		return newKnowledge;
	}
}
