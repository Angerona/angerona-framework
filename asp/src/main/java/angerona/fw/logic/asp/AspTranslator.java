package angerona.fw.logic.asp;

import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.logicprogramming.asplibrary.syntax.Atom;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Neg;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Program;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Rule;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import net.sf.tweety.logics.firstorderlogic.syntax.Negation;
import angerona.fw.BaseBeliefbase;
import angerona.fw.Perception;
import angerona.fw.comm.Answer;
import angerona.fw.logic.AngeronaAnswer;
import angerona.fw.logic.AnswerValue;
import angerona.fw.logic.BaseTranslator;

/**
 * Default translator which generates elp programs
 * encapsulated in AspBeliefbase.
 * 
 * @author Tim Janus
 */
public class AspTranslator extends BaseTranslator {

	@Override
	protected BaseBeliefbase translatePerceptionInt(Perception p) {
		AspBeliefbase reval = new AspBeliefbase();
		Set<FolFormula>  formulas = new HashSet<FolFormula>();
		
		if(p instanceof Answer) {
			Answer answer = (Answer)p;
			AngeronaAnswer aa = answer.getAnswer();
			if(aa.getAnswerValue() == AnswerValue.AV_COMPLEX) {
				return translateFOL(aa.getAnswers());
			} else {
				FolFormula knowledge = answer.getRegarding();
				if(aa.getAnswerValue() == AnswerValue.AV_FALSE) {
					knowledge = new Negation(knowledge);
				} else if(aa.getAnswerValue() == AnswerValue.AV_UNKNOWN) {
					return reval;
				}
				formulas.add(knowledge);
			}
		} 

		return translateFOLInt(formulas);
	}

	@Override
	protected BaseBeliefbase translateFOLInt(Set<FolFormula> formulas) {
		AspBeliefbase reval = new AspBeliefbase();
		reval.setProgram(translate(formulas));
		return reval;
	}

	private Atom createAtom(FolFormula ff, boolean truth)
	{
		Atom a = null;
		if(truth)
			a = new Atom(ff.toString());
		else
			a = new Atom(ff.toString().substring(1));
			
		return a;
	}
	

	private Program translate(Set<FolFormula> formulas) {
		Program newInfo = new Program();
		for(FolFormula ff : formulas) {
			Rule r = new Rule();
			
			if(ff instanceof net.sf.tweety.logics.firstorderlogic.syntax.Atom) {
				Atom newAtom = createAtom(ff, true);
				r.addHead(newAtom);
			} else if(ff instanceof Negation) {
				Atom newAtom = createAtom(ff, false);
				r.addHead(new Neg(newAtom));
				
			} else {
				continue;
			}
			newInfo.add(r);
		}
		return newInfo;
	}
}
