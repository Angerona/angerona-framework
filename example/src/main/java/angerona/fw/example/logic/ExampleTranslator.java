package angerona.fw.example.logic;

import net.sf.tweety.logicprogramming.nlp.syntax.NLPProgram;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import net.sf.tweety.logics.firstorderlogic.syntax.Negation;
import net.sf.tweety.logics.translate.aspnlp.AspNlpTranslator;
import angerona.fw.BaseBeliefbase;
import angerona.fw.Perception;
import angerona.fw.comm.Answer;
import angerona.fw.error.NotImplementedException;
import angerona.fw.logic.AnswerValue;
import angerona.fw.logic.BaseTranslator;

/**
 * A simple default translator which translate from FOL to FOL... it also
 * supports the handling of the Answer Speech act.
 * @author Tim Janus
 */
public class ExampleTranslator extends BaseTranslator {

	private static AspNlpTranslator translator = new AspNlpTranslator();
	
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
