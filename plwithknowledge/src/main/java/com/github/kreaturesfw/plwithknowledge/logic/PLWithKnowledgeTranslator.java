package com.github.kreaturesfw.plwithknowledge.logic;

import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.logics.fol.syntax.FOLAtom;
import net.sf.tweety.logics.fol.syntax.FolFormula;
import net.sf.tweety.logics.fol.syntax.Negation;
import net.sf.tweety.logics.pl.syntax.Proposition;
import net.sf.tweety.lp.nlp.syntax.NLPProgram;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.kreaturesfw.core.comm.Answer;
import com.github.kreaturesfw.core.comm.Update;
import com.github.kreaturesfw.core.legacy.BaseBeliefbase;
import com.github.kreaturesfw.core.legacy.Perception;
import com.github.kreaturesfw.core.logic.BaseTranslator;

/**
 * Translator for propositional beliefbase containing knowledge and assertions
 * 
 * @author Pia Wierzoch
 */
public class PLWithKnowledgeTranslator extends BaseTranslator{

	/** reference to the logback instance used for logging */
	private static Logger LOG = LoggerFactory.getLogger(PLWithKnowledgeTranslator.class);
	
	@Override
	protected BaseBeliefbase translatePerceptionImpl(BaseBeliefbase caller,
			Perception p) {
		Set<FolFormula>  formulas = new HashSet<FolFormula>();
		
		if (p instanceof Update){
			formulas.add(((Update)p).getProposition());
		}else if(p instanceof Answer){
				return new PLWithKnowledgeBeliefbase();
		}
		return translateFOL(caller, formulas);
	}
		
		

	@Override
	protected BaseBeliefbase translateNLPImpl(BaseBeliefbase caller,
			NLPProgram program) {
		PLWithKnowledgeBeliefbase reval = new PLWithKnowledgeBeliefbase();
		for(FolFormula formula : program.getFacts()) {
			if(formula instanceof FOLAtom) {
				FOLAtom atom = (FOLAtom) formula;
				Proposition p = new Proposition(atom.getPredicate().getName());
				reval.getAssertions().addLast(p);
			} else if( formula instanceof Negation) {
				Negation neg = (Negation) formula;
				FolFormula negatedFormula = neg.getFormula();
				if(negatedFormula instanceof FOLAtom) {
					FOLAtom atom = (FOLAtom) negatedFormula;
					Proposition p = new Proposition(atom.getPredicate().getName());
					reval.getAssertions().addLast(new net.sf.tweety.logics.pl.syntax.Negation(p));
				} else {
					LOG.warn("Translation of invalid formula '{}'", formula);
				}
			}
		}
		return reval;
	}

	@Override
	protected BaseBeliefbase defaultReturnValue() {
		return new PLWithKnowledgeBeliefbase();
	}

}
