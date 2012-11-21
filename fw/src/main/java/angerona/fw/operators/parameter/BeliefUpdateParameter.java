package angerona.fw.operators.parameter;

import angerona.fw.Agent;
import angerona.fw.BaseBeliefbase;

/**
 * Parameter class for change operations on belief bases.
 * @author Tim Janus
 *
 */
public class BeliefUpdateParameter extends GenericOperatorParameter {
	/** Reference a belief base. The update operation will be performed on this belief base.*/
	private BaseBeliefbase beliefBase;
	
	/** belief base representing the new knowledge. */
	private BaseBeliefbase newKnowledge;
	
	/**
	 * Ctor: Generates the Parameter class
	 * @param bb			belief base
	 * @param newKnowledge	set of formula with new knowledge.
	 */
	public BeliefUpdateParameter(Agent agContext, BaseBeliefbase bb, BaseBeliefbase newKnowledge) {
		super(agContext);
		this.beliefBase = bb;
		this.newKnowledge = newKnowledge;
	}

	/** @return the belief base on which the update operation will be performed */
	public BaseBeliefbase getBeliefBase() {
		return beliefBase;
	}

	/** @return a set of formulas representing the new knowledge */
	public BaseBeliefbase getNewKnowledge() {
		return newKnowledge;
	}
}
