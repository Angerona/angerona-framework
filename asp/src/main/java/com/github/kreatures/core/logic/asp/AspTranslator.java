package com.github.kreatures.core.logic.asp;

import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.logics.fol.syntax.FolFormula;
import net.sf.tweety.logics.fol.syntax.Negation;
import net.sf.tweety.logics.translators.aspnlp.AspNlpTranslator;
import net.sf.tweety.lp.nlp.syntax.NLPProgram;

import com.github.kreatures.core.BaseBeliefbase;
import com.github.kreatures.core.Perception;
import com.github.kreatures.core.comm.Answer;
import com.github.kreatures.core.comm.Inform;
import com.github.kreatures.core.comm.Query;
import com.github.kreatures.core.logic.KReaturesAnswer;
import com.github.kreatures.core.logic.AnswerValue;
import com.github.kreatures.core.logic.BaseTranslator;

/**
 * Default translator which generates ELPs 
 * encapsulated in AspBeliefbase.
 * 
 * @author Tim Janus
 */
public class AspTranslator extends BaseTranslator {
	/** reference to the logback instance used for logging */
	//private static Logger LOG = LoggerFactory.getLogger(AspTranslator.class);
	
	private static AspNlpTranslator translator = new AspNlpTranslator();
	
	@Override
	protected AspBeliefbase translatePerceptionImpl(BaseBeliefbase caller, Perception p) {
		AspBeliefbase reval = new AspBeliefbase();
		Set<FolFormula>  formulas = new HashSet<FolFormula>();
		
		if(p instanceof Answer) {
			Answer answer = (Answer)p;
			KReaturesAnswer aa = answer.getAnswer();
			if(aa.getAnswerValue() == AnswerValue.AV_COMPLEX) {
				return (AspBeliefbase)translateFOL(caller, aa.getAnswers());
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

		return (AspBeliefbase)translateFOL(caller, formulas);
	}

	@Override
	protected AspBeliefbase translateNLPImpl(BaseBeliefbase caller, NLPProgram program) {
		AspBeliefbase reval = new AspBeliefbase();
		reval.setProgram(translator.toASP(program));
		return reval;
	}

	@Override
	protected AspBeliefbase defaultReturnValue() {
		return new AspBeliefbase();
	}
}
