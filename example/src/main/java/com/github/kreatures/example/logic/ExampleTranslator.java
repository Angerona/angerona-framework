package com.github.kreatures.example.logic;

import java.util.Set;

import net.sf.tweety.logics.fol.syntax.FolFormula;
import net.sf.tweety.logics.fol.syntax.Negation;
import net.sf.tweety.lp.nlp.syntax.NLPProgram;

import com.github.kreatures.core.BaseBeliefbase;
import com.github.kreatures.core.Perception;
import com.github.kreatures.core.comm.Answer;
import com.github.kreatures.core.comm.Inform;
import com.github.kreatures.core.comm.Query;
import com.github.kreatures.core.error.NotImplementedException;
import com.github.kreatures.core.logic.AnswerValue;
import com.github.kreatures.core.logic.BaseTranslator;

/**
 * A simple default translator which translate from FOL to FOL... it also
 * supports the handling of the Answer Speech act.
 * @author Tim Janus
 */
public class ExampleTranslator extends BaseTranslator {

	@Override
	protected ExampleBeliefbase translatePerceptionImpl(BaseBeliefbase caller, Perception p) {
		// Translate the knowledge encoded in the answer into a dummy belief base.
		FolFormula formula = null;
		if(p instanceof Answer) {
			Answer a = (Answer)p;
			if(a.getAnswer().getAnswerValue() == AnswerValue.AV_TRUE) {
				formula = a.getRegarding();
			} else if(a.getAnswer().getAnswerValue() == AnswerValue.AV_FALSE) {
				formula = new Negation(a.getRegarding());
			} else {
				return new ExampleBeliefbase();
			}
			return (ExampleBeliefbase)translateFOL(caller, formula);
		} else if(p instanceof Inform) {
			Inform i = (Inform)p;
			Set<FolFormula> sentences = i.getSentences();
			if (sentences.iterator().hasNext()) {
				formula = sentences.iterator().next();
				return (ExampleBeliefbase)translateFOL(caller, formula);
			}
		} else if (p instanceof Query) {
			Query q = (Query)p;
			formula = (q.getQuestion());
			return (ExampleBeliefbase)translateFOL(caller, formula);
		}
		
		// nothing but answer implemented yet.
		throw new NotImplementedException();
	}

	@Override
	protected ExampleBeliefbase translateNLPImpl(BaseBeliefbase caller, NLPProgram program) {
		ExampleBeliefbase reval = new ExampleBeliefbase();
		reval.fbs.addAll(program.getFacts());
		return reval;
	}

	@Override
	protected ExampleBeliefbase defaultReturnValue() {
		return new ExampleBeliefbase();
	}
}
