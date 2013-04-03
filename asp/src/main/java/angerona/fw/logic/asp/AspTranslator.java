package angerona.fw.logic.asp;

import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.logicprogramming.asplibrary.syntax.Atom;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Literal;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Neg;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Program;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Rule;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import net.sf.tweety.logics.firstorderlogic.syntax.Negation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.BaseBeliefbase;
import angerona.fw.Perception;
import angerona.fw.comm.Answer;
import angerona.fw.comm.Inform;
import angerona.fw.comm.Query;
import angerona.fw.logic.AngeronaAnswer;
import angerona.fw.logic.AnswerValue;
import angerona.fw.logic.BaseTranslator;

/**
 * Default translator which generates ELPs 
 * encapsulated in AspBeliefbase.
 * 
 * @author Tim Janus
 */
public class AspTranslator extends BaseTranslator {
	/** reference to the logback instance used for logging */
	private static Logger LOG = LoggerFactory.getLogger(AspTranslator.class);
	
	@Override
	protected BaseBeliefbase translatePerceptionInt(BaseBeliefbase caller, Perception p) {
		AspBeliefbase reval = new AspBeliefbase();
		Set<FolFormula>  formulas = new HashSet<FolFormula>();
		
		if(p instanceof Answer) {
			Answer answer = (Answer)p;
			AngeronaAnswer aa = answer.getAnswer();
			if(aa.getAnswerValue() == AnswerValue.AV_COMPLEX) {
				return translateFOLInt(caller, aa.getAnswers());
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

		return translateFOLInt(caller, formulas);
	}

	@Override
	protected BaseBeliefbase translateFOLInt(BaseBeliefbase caller, Set<FolFormula> formulas) {
		AspBeliefbase reval = new AspBeliefbase();
		reval.setProgram(translate(formulas));
		return reval;
	}

	private Literal createLiteral(FolFormula ff, boolean truth)
	{
		Literal a = null;
		if(truth)
			a = new Atom(ff.toString());
		else
			a = new Neg(new Atom(ff.toString().substring(1)));
			
		return a;
	}
	

	private Program translate(Set<FolFormula> formulas) {
		Program newInfo = new Program();
		for(FolFormula ff : formulas) {
			Rule r = new Rule();
			
			if(ff instanceof net.sf.tweety.logics.firstorderlogic.syntax.Atom) {
				Literal newAtom = createLiteral(ff, true);
				r.addHead(newAtom);
			} else if(ff instanceof Negation) {
				Literal newAtom = createLiteral(ff, false);
				r.addHead(newAtom);
				
			} else {
				continue;
			}
			if(r.isSafe())
				newInfo.add(r);
			else
				LOG.warn("Translate skips the unsafe rule: '{}', might produce strange behavior.", r.toString());
				
		}
		return newInfo;
	}
}
