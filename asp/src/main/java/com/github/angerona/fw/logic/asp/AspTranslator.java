package com.github.angerona.fw.logic.asp;

import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.logicprogramming.nlp.syntax.NLPProgram;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import net.sf.tweety.logics.firstorderlogic.syntax.Negation;
import net.sf.tweety.logics.translate.aspnlp.AspNlpTranslator;

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
 * Default translator which generates ELPs 
 * encapsulated in AspBeliefbase.
 * 
 * @author Tim Janus
 */
public class AspTranslator extends BaseTranslator {
	/** reference to the logback instance used for logging */
	private static Logger LOG = LoggerFactory.getLogger(AspTranslator.class);
	
	private static AspNlpTranslator translator = new AspNlpTranslator();
	
	@Override
	protected AspBeliefbase translatePerceptionInt(BaseBeliefbase caller, Perception p) {
		AspBeliefbase reval = new AspBeliefbase();
		Set<FolFormula>  formulas = new HashSet<FolFormula>();
		
		if(p instanceof Answer) {
			Answer answer = (Answer)p;
			AngeronaAnswer aa = answer.getAnswer();
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
	protected AspBeliefbase translateNLPInt(BaseBeliefbase caller, NLPProgram program) {
		AspBeliefbase reval = new AspBeliefbase();
		reval.setProgram(translator.toASP(program));
		return reval;
	}
}
