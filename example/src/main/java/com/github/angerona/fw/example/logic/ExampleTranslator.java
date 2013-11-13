package com.github.angerona.fw.example.logic;

import net.sf.tweety.logics.fol.syntax.FolFormula;
import net.sf.tweety.logics.fol.syntax.Negation;
import net.sf.tweety.lp.nlp.syntax.NLPProgram;

import com.github.angerona.fw.BaseBeliefbase;
import com.github.angerona.fw.Perception;
import com.github.angerona.fw.comm.Answer;
import com.github.angerona.fw.error.NotImplementedException;
import com.github.angerona.fw.logic.AnswerValue;
import com.github.angerona.fw.logic.BaseTranslator;

/**
 * A simple default translator which translate from FOL to FOL... it also
 * supports the handling of the Answer Speech act.
 * @author Tim Janus
 */
public class ExampleTranslator extends BaseTranslator {

	@Override
	protected BaseBeliefbase translatePerceptionInt(BaseBeliefbase caller, Perception p) {
		// Translate the knowledge encoded in the answer into a dummy belief base.
		if(p instanceof Answer) {
			FolFormula formula = null;
			Answer a = (Answer)p;
			if(a.getAnswer().getAnswerValue() == AnswerValue.AV_TRUE) {
				formula = a.getRegarding();
			} else if(a.getAnswer().getAnswerValue() == AnswerValue.AV_FALSE) {
				formula = new Negation(a.getRegarding());
			} else {
				return new ExampleBeliefbase();
			}
			return translateFOL(caller, formula);
		}
		
		// nothing but answer implemented yet.
		throw new NotImplementedException();
	}

	@Override
	protected BaseBeliefbase translateNLPInt(BaseBeliefbase caller, NLPProgram program) {
		ExampleBeliefbase reval = new ExampleBeliefbase();
		reval.fbs.addAll(program.getFacts());
		return reval;
	}
}
