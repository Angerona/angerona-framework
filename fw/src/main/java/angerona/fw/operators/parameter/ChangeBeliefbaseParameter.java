package angerona.fw.operators.parameter;

import angerona.fw.BaseBeliefbase;

/**
 * Parameter class for change operations on belief bases.
 * @author Tim Janus
 *
 */
public class ChangeBeliefbaseParameter extends BeliefbasePluginParameter {
	/** belief base representing the new knowledge. */
	private BaseBeliefbase newKnowledge;
	
	public ChangeBeliefbaseParameter() {}
	
	/**
	 * Ctor: Generates the Parameter class
	 * @param source		belief base
	 * @param newKnowledge	set of formula with new knowledge.
	 */
	public ChangeBeliefbaseParameter(BaseBeliefbase source, BaseBeliefbase newKnowledge) {
		super(source);
		this.newKnowledge = newKnowledge;
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
