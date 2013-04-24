package angerona.fw.util;

import java.util.HashSet;
import java.util.Set;

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
	
	/**
	 * Translates a closed first order formula with zero-arity predicates to propositional formula.
	 * Treats predicates with non-zero arity as zero-arity!
	 * @param formula
	 * @return formula in propositional syntax
	 */
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
	
	/**
	 * Translates a set of closed first order formulae with zero-arity predicates to propositional formulae.
	 * Treats predicates with non-zero arity as zero-arity!
	 * @param formulas
	 * @return formulae in propositional syntax
	 */
	public static Set<PropositionalFormula> FoToPl(Set<RelationalFormula> formulas) {
		HashSet<PropositionalFormula> result = new HashSet<PropositionalFormula>();
		for(RelationalFormula formula : formulas) {
			result.add(FoToPl(formula));
		}
		return result;
	}
}
