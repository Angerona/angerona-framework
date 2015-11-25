package com.github.kreaturesfw.plwithknowledge.logic;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import net.sf.tweety.logics.fol.syntax.FolFormula;
import net.sf.tweety.logics.pl.PlBeliefSet;
//import net.sf.tweety.logics.pl.sat.Sat4jSolver;
import net.sf.tweety.logics.pl.syntax.PropositionalFormula;
import net.sf.tweety.logics.translators.folprop.FOLPropTranslator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.kreaturesfw.core.BaseBeliefbase;
import com.github.kreaturesfw.core.logic.BaseChangeBeliefs;
import com.github.kreaturesfw.core.operators.parameter.ChangeBeliefbaseParameter;

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
		if(!newKnowledge.getAssertions().isEmpty()){
			LinkedList<PropositionalFormula> context = beliefbase.getAssertions();
			PropositionalFormula formula = newKnowledge.getAssertions().getLast();
			context.addLast(formula);
			beliefbase.setAssertions(context);
		}
		return beliefbase;
	}
	
	public BaseBeliefbase processImpl(BaseBeliefbase bbase, PropositionalFormula formula){
		log.info("Update by '{}'", formula);
		PLWithKnowledgeBeliefbase beliefbase = (PLWithKnowledgeBeliefbase) bbase;
		if(formula != null){
			LinkedList<PropositionalFormula> context = beliefbase.getAssertions();
			context.addLast(formula);
			beliefbase.setAssertions(context);
		}
		return beliefbase;
	}

	@Override
	public Class<? extends BaseBeliefbase> getSupportedBeliefbase() {
		return PLWithKnowledgeBeliefbase.class;
	}
	
	public boolean simulateUpdate(FolFormula formula, Set<PropositionalFormula> knowledge){
		log.info("Simulating update by '{}'", formula);
		
		Set<PropositionalFormula> k = new HashSet<PropositionalFormula>(knowledge);
		FOLPropTranslator translator = new FOLPropTranslator();
		//Sat4jSolver solver = new Sat4jSolver();
		k.add(translator.toPropositional(formula));
		PlBeliefSet bset = new PlBeliefSet(k);
		return bset.isConsistent();
		
		//return solver.isSatisfiable(k);
	}

}
