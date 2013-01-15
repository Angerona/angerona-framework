package angerona.fw.logic.conditional;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.tweety.logics.firstorderlogic.syntax.Atom;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import net.sf.tweety.logics.firstorderlogic.syntax.Negation;
import net.sf.tweety.logics.propositionallogic.syntax.Proposition;
import angerona.fw.BaseBeliefbase;
import angerona.fw.Perception;
import angerona.fw.comm.Answer;
import angerona.fw.comm.Inform;
import angerona.fw.comm.Query;
import angerona.fw.logic.AngeronaAnswer;
import angerona.fw.logic.AnswerValue;
import angerona.fw.logic.BaseTranslator;

/**
 * Default translator for conditional belief bases
 * @author Sebastian Homann, Pia Wierzoch
 */
public class ConditionalTranslator extends BaseTranslator {
	
	/** reference to the logback instance used for logging */
	private static Logger LOG = LoggerFactory.getLogger(ConditionalTranslator.class);

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

		return translateFOLInt(caller, formulas);
	}

	/**
	 * Translates a set of first order formulas to a conditional belief base.
	 * Each formula is interpreted as one propositional literal.
	 */
	@Override
	protected BaseBeliefbase translateFOLInt(BaseBeliefbase caller, Set<FolFormula> formulas) {
		ConditionalBeliefbase reval = new ConditionalBeliefbase();
		
		for(FolFormula formula : formulas) {
			if(formula instanceof Atom) {
				Atom atom = (Atom) formula;
				Proposition p = new Proposition(atom.getPredicate().getName());
				reval.getPropositions().add(p);
			} else if( formula instanceof Negation) {
				Negation neg = (Negation) formula;
				FolFormula negatedFormula = neg.getFormula();
				if(negatedFormula instanceof Atom) {
					Atom atom = (Atom) negatedFormula;
					Proposition p = new Proposition(atom.getPredicate().getName());
					reval.getPropositions().add(new net.sf.tweety.logics.propositionallogic.syntax.Negation(p));
				} else {
					LOG.warn("Translation of invalid formula '{}', literal expected", formula);
				}
			}
		}
		return reval;
	}

}
