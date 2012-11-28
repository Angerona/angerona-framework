package angerona.fw.logic.conditional;

import java.util.Set;

import net.sf.tweety.logics.propositionallogic.syntax.PropositionalFormula;
import angerona.fw.BaseBeliefbase;
import angerona.fw.logic.BaseChangeBeliefs;
import angerona.fw.operators.parameter.BeliefUpdateParameter;

/**
 * Simple expansion operator for conditional belief bases without consistency-checks
 * @author Sebastian Homann, Pia Wierzoch
 */
public class ConditionalExpansion extends BaseChangeBeliefs {

	@Override
	public Class<? extends BaseBeliefbase> getSupportedBeliefbase() {
		return ConditionalBeliefbase.class;
	}

	/**
	 * Add all propositions from the new knowledge to the existing belief base
	 */
	@Override
	protected BaseBeliefbase processInt(BeliefUpdateParameter param) {
		ConditionalBeliefbase beliefbase = (ConditionalBeliefbase) param.getBeliefBase();
		ConditionalBeliefbase newKnowledge = (ConditionalBeliefbase) param.getNewKnowledge();
		
		Set<PropositionalFormula> propositions = beliefbase.getPropositions();
		propositions.addAll( newKnowledge.getPropositions() );
		
		return param.getBeliefBase();
	}

}
