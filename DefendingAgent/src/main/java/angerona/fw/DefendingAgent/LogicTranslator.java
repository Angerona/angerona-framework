package angerona.fw.DefendingAgent;

import net.sf.tweety.logics.firstorderlogic.syntax.Atom;
import net.sf.tweety.logics.firstorderlogic.syntax.Conjunction;
import net.sf.tweety.logics.firstorderlogic.syntax.Disjunction;
import net.sf.tweety.logics.firstorderlogic.syntax.Negation;
import net.sf.tweety.logics.firstorderlogic.syntax.RelationalFormula;
import net.sf.tweety.logics.propositionallogic.syntax.Proposition;
import net.sf.tweety.logics.propositionallogic.syntax.PropositionalFormula;

/**
 * Helper class to translate closed first order formulas with zero-arity predicates
 * to propositional formulas.
 * @author Sebastian Homann, Pia Wierzoch
 *
 */
public class LogicTranslator {
	public static PropositionalFormula FoToPl(RelationalFormula formula) {
		
		if(formula instanceof Atom) {
			Atom atom = (Atom) formula;
			return new Proposition(atom.getPredicate().getName());
		} else if( formula instanceof Negation) {
			Negation neg = (Negation) formula;
			RelationalFormula negatedFormula = neg.getFormula();
			return new net.sf.tweety.logics.propositionallogic.syntax.Negation(FoToPl(negatedFormula));
		} else if( formula instanceof Conjunction) {
			Conjunction conj = (Conjunction) formula;
			net.sf.tweety.logics.propositionallogic.syntax.Conjunction result = new net.sf.tweety.logics.propositionallogic.syntax.Conjunction();
			for(RelationalFormula relform : conj) {
				result.add(FoToPl(relform));
			}
			return result;
		} else if( formula instanceof Disjunction) {
			Disjunction conj = (Disjunction) formula;
			net.sf.tweety.logics.propositionallogic.syntax.Disjunction result = new net.sf.tweety.logics.propositionallogic.syntax.Disjunction();
			for(RelationalFormula relform : conj) {
				result.add(FoToPl(relform));
			}
			return result;
		}
		return null;
	}
}
