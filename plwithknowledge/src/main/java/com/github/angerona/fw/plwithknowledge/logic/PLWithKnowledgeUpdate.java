package com.github.angerona.fw.plwithknowledge.logic;

import java.util.LinkedList;

import net.sf.tweety.logics.pl.syntax.PropositionalFormula;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.angerona.fw.BaseBeliefbase;
import com.github.angerona.fw.logic.BaseChangeBeliefs;
import com.github.angerona.fw.operators.parameter.ChangeBeliefbaseParameter;

/**
 * Update operator for propositional beliefbase containing knowledge and assertions.
 * 
 * @author Pia Wierzoch
 */
public class PLWithKnowledgeUpdate extends BaseChangeBeliefs {

	/** reference to the logging facility */
	private static Logger log = LoggerFactory.getLogger(PLWithKnowledgeUpdate.class);
	
	@Override
	protected BaseBeliefbase processImpl(ChangeBeliefbaseParameter in) {
		log.info("Update by '{}'", in.getNewKnowledge());
		PLWithKnowledgeBeliefbase beliefbase = (PLWithKnowledgeBeliefbase) in.getBeliefBase();
		PLWithKnowledgeBeliefbase newKnowledge = (PLWithKnowledgeBeliefbase) in.getNewKnowledge();
		
		LinkedList<PropositionalFormula> context = beliefbase.getAssertions();
		context.addLast(newKnowledge.getAssertions().getLast());
		beliefbase.setAssertions(context); 
		return beliefbase;
	}

	@Override
	public Class<? extends BaseBeliefbase> getSupportedBeliefbase() {
		return PLWithKnowledgeBeliefbase.class;
	}
}

