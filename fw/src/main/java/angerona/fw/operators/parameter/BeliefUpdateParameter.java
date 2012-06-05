package angerona.fw.operators.parameter;

import java.util.Set;

import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import angerona.fw.BaseBeliefbase;
import angerona.fw.operators.OperatorVisitor;

/**
 * Parameter class for Expansion and Revision Operations
 * on belief bases.
 * @author Tim Janus
 *
 */
public class BeliefUpdateParameter extends GenericOperatorParameter {
	/** Reference a belief base. The update operation will be performed on this belief base.*/
	private BaseBeliefbase beliefBase;
	
	/** set of formulas representing the new knowledge */
	private Set<FolFormula> newKnowledge;
	
	/**
	 * Ctor: Generates the Parameter class
	 * @param bb			belief base
	 * @param newKnowledge	set of formula with new knowledge.
	 */
	public BeliefUpdateParameter(BaseBeliefbase bb, Set<FolFormula> newKnowledge, OperatorVisitor ov) {
		super(ov);
		this.beliefBase = bb;
		this.newKnowledge = newKnowledge;
	}

	/** @return the belief base on which the update operation will be performed */
	public BaseBeliefbase getBeliefBase() {
		return beliefBase;
	}

	/** @return a set of formulas representing the new knowledge */
	public Set<FolFormula> getNewKnowledge() {
		return newKnowledge;
	}
}
