package angerona.fw.logic.asp;

import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.logicprogramming.asplibrary.syntax.Atom;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Neg;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Program;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Rule;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import net.sf.tweety.logics.firstorderlogic.syntax.Negation;
import net.sf.tweety.logics.firstorderlogic.syntax.Predicate;
import angerona.fw.BaseBeliefbase;
import angerona.fw.Perception;
import angerona.fw.comm.Answer;
import angerona.fw.comm.DetailQueryAnswer;
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
		
		if(p instanceof DetailQueryAnswer) {
			DetailQueryAnswer da = (DetailQueryAnswer)p;
			formulas.add(da.getDetailAnswer());
		}
		else if(p instanceof Answer) {
			Answer answer = (Answer)p;
			FolFormula knowledge = answer.getRegarding();
			if(answer.getAnswer() == AnswerValue.AV_FALSE) {
				knowledge = new Negation(knowledge);
			} else if(answer.getAnswer() == AnswerValue.AV_UNKNOWN) {
				return reval;
			}

			formulas.add(knowledge);
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
				net.sf.tweety.logics.firstorderlogic.syntax.Atom a = 
						(net.sf.tweety.logics.firstorderlogic.syntax.Atom)ff;

				Atom newAtom = createAtom(ff, true);
				
				r.addHead(newAtom);
				
			} else if(ff instanceof Negation) {
				Negation n = (Negation)ff;

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
