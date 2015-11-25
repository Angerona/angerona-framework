package com.github.kreatures.core.logic.ocf;

import java.util.Set;

import net.sf.tweety.logics.pl.syntax.PropositionalFormula;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.kreatures.core.BaseBeliefbase;
import com.github.kreatures.core.logic.BaseChangeBeliefs;
import com.github.kreatures.core.operators.parameter.ChangeBeliefbaseParameter;

/**
 * Simple expansion operator for ocf belief bases without consistency-checks
 * @author Sebastian Homann, Pia Wierzoch
 */
public class ConditionalExpansion extends BaseChangeBeliefs {
	/** reference to the logging facility */
	private static Logger log = LoggerFactory.getLogger(ConditionalExpansion.class);

	@Override
	public Class<? extends BaseBeliefbase> getSupportedBeliefbase() {
		return ConditionalBeliefbase.class;
	}

	/**
	 * Add all propositions from the new knowledge to the existing belief base
	 */
	@Override
	protected BaseBeliefbase processImpl(ChangeBeliefbaseParameter param) {
		log.info("Expansion with '{}'", param.getNewKnowledge());
		ConditionalBeliefbase beliefbase = (ConditionalBeliefbase) param.getBeliefBase();
		ConditionalBeliefbase newKnowledge = (ConditionalBeliefbase) param.getNewKnowledge();
		
		Set<PropositionalFormula> propositions = beliefbase.getPropositions();
		propositions.addAll( newKnowledge.getPropositions() );
		
		return param.getBeliefBase();
	}

}
