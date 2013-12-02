package com.github.angerona.fw.logic.conditional;

import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.logics.fol.syntax.FOLAtom;
import net.sf.tweety.logics.fol.syntax.FolFormula;
import net.sf.tweety.logics.fol.syntax.Negation;
import net.sf.tweety.logics.pl.syntax.Proposition;
import net.sf.tweety.logics.translators.clnlp.ClNLPTranslator;
import net.sf.tweety.lp.nlp.syntax.NLPProgram;
import net.sf.tweety.lp.nlp.syntax.NLPRule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.angerona.fw.BaseBeliefbase;
import com.github.angerona.fw.Perception;
import com.github.angerona.fw.comm.Answer;
import com.github.angerona.fw.comm.Inform;
import com.github.angerona.fw.comm.Query;
import com.github.angerona.fw.logic.AngeronaAnswer;
import com.github.angerona.fw.logic.AnswerValue;
import com.github.angerona.fw.logic.BaseTranslator;

/**
 * Default translator for conditional belief bases
 * @author Sebastian Homann, Pia Wierzoch, Tim Janus (modifications)
 */
public class ConditionalTranslator extends BaseTranslator {
	
	/** reference to the logback instance used for logging */
	private static Logger LOG = LoggerFactory.getLogger(ConditionalTranslator.class);
	private ClNLPTranslator cl2nlp = new ClNLPTranslator();
	/**
	 * Translates a perception into a beliefbase. A perception may contain
	 * formulas in first order logic, which have to be translated to a conditional
	 * belief base.
	 */
	@Override
	protected BaseBeliefbase translatePerceptionInt(BaseBeliefbase caller, Perception p) {
		ConditionalBeliefbase reval = new ConditionalBeliefbase();
		Set<FolFormula>  formulas = new HashSet<FolFormula>();
		
		if(p instanceof Answer) {
			Answer answer = (Answer)p;
			AngeronaAnswer aa = answer.getAnswer();
			if(aa.getAnswerValue() == AnswerValue.AV_COMPLEX) {
				return translateFOL(caller, aa.getAnswers());
			} else {
				FolFormula knowledge = answer.getRegarding();
				if(aa.getAnswerValue() == AnswerValue.AV_FALSE) {
					knowledge = new Negation(knowledge);
				} else if(aa.getAnswerValue() == AnswerValue.AV_UNKNOWN) {
					return reval;
				}
				formulas.add(knowledge);
			}
		} else if(p instanceof Inform) {
			Inform i = (Inform)p;
			formulas.addAll(i.getSentences());
		} else if (p instanceof Query) {
			Query q = (Query)p;
			formulas.add(q.getQuestion());
		}

		return translateFOL(caller, formulas);
	}

	/**
	 * Translates a set of facts contained in a nlp-program to a conditional belief base.
	 * Each formula is interpreted as one propositional literal.
	 */
	@Override
	protected BaseBeliefbase translateNLPInt(BaseBeliefbase caller, NLPProgram program) {
		ConditionalBeliefbase reval = new ConditionalBeliefbase();
		
		// uncomment the following line if all nlp-rules should be
	    // translated to conditionals
//		reval.setConditionalBeliefs(cl2nlp.toCl(program));
		
		for(FolFormula formula : program.getFacts()) {
			if(formula instanceof FOLAtom) {
				FOLAtom atom = (FOLAtom) formula;
				Proposition p = new Proposition(atom.getPredicate().getName());
				reval.getPropositions().add(p);
			} else if( formula instanceof Negation) {
				Negation neg = (Negation) formula;
				FolFormula negatedFormula = neg.getFormula();
				if(negatedFormula instanceof FOLAtom) {
					FOLAtom atom = (FOLAtom) negatedFormula;
					Proposition p = new Proposition(atom.getPredicate().getName());
					reval.getPropositions().add(new net.sf.tweety.logics.pl.syntax.Negation(p));
				} else {
					LOG.warn("Translation of invalid formula '{}', literal expected", formula);
				}
			}
		}
		
		return reval;
	}

}
