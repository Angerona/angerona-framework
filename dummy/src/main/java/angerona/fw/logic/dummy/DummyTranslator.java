package angerona.fw.logic.dummy;

import java.util.Set;

import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import net.sf.tweety.logics.firstorderlogic.syntax.Negation;
import angerona.fw.BaseBeliefbase;
import angerona.fw.Perception;
import angerona.fw.comm.Answer;
import angerona.fw.error.NotImplementedException;
import angerona.fw.logic.AnswerValue;
import angerona.fw.logic.BaseTranslator;
import angerona.fw.operators.parameter.TranslatorParameter;

/**
 * A simple default translator which translate from FOL to FOL... it also
 * supports the handling of the Answer Speech act.
 * @author Tim Janus
 */
public class DummyTranslator extends BaseTranslator {

	@Override
	protected BaseBeliefbase translatePerceptionInt(Perception p) {
		// Translate the knowledge encoded in the answer into a dummy belief base.
		if(p instanceof Answer) {
			FolFormula formula = null;
			Answer a = (Answer)p;
			if(a.getAnswer().getAnswerValue() == AnswerValue.AV_TRUE) {
				formula = a.getRegarding();
			} else if(a.getAnswer().getAnswerValue() == AnswerValue.AV_FALSE) {
				formula = new Negation(a.getRegarding());
			} else {
				return new DummyBeliefbase();
			}
			return translateFOLInt(formula);
		}
		
		// nothing but answer implemented yet.
		throw new NotImplementedException();
	}

	@Override
	protected BaseBeliefbase translateFOLInt(Set<FolFormula> formulas) {
		DummyBeliefbase reval = new DummyBeliefbase();
		reval.fbs.addAll(formulas);
		return reval;
	}
}
