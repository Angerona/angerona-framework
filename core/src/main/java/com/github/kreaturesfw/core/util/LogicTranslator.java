package com.github.kreaturesfw.core.util;

import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.logics.commons.syntax.Predicate;
import net.sf.tweety.logics.fol.syntax.Conjunction;
import net.sf.tweety.logics.fol.syntax.Contradiction;
import net.sf.tweety.logics.fol.syntax.Disjunction;
import net.sf.tweety.logics.fol.syntax.FOLAtom;
import net.sf.tweety.logics.fol.syntax.FolFormula;
import net.sf.tweety.logics.fol.syntax.Negation;
import net.sf.tweety.logics.fol.syntax.RelationalFormula;
import net.sf.tweety.logics.fol.syntax.Tautology;
import net.sf.tweety.logics.pl.syntax.Proposition;
import net.sf.tweety.logics.pl.syntax.PropositionalFormula;

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
		
		if(formula instanceof FOLAtom) {
			FOLAtom atom = (FOLAtom) formula;
			return new Proposition(atom.getPredicate().getName());
		} else if( formula instanceof Negation) {
			Negation neg = (Negation) formula;
			RelationalFormula negatedFormula = neg.getFormula();
			return new net.sf.tweety.logics.pl.syntax.Negation(FoToPl(negatedFormula));
		} else if( formula instanceof Conjunction) {
			Conjunction conj = (Conjunction) formula;
			net.sf.tweety.logics.pl.syntax.Conjunction result = new net.sf.tweety.logics.pl.syntax.Conjunction();
			for(RelationalFormula relform : conj) {
				result.add(FoToPl(relform));
			}
			return result;
		} else if( formula instanceof Disjunction) {
			Disjunction conj = (Disjunction) formula;
			net.sf.tweety.logics.pl.syntax.Disjunction result = new net.sf.tweety.logics.pl.syntax.Disjunction();
			for(RelationalFormula relform : conj) {
				result.add(FoToPl(relform));
			}
			return result;
		} else if( formula instanceof Tautology) {
			return new net.sf.tweety.logics.pl.syntax.Tautology();
		} else if( formula instanceof Contradiction) {
			return new net.sf.tweety.logics.pl.syntax.Tautology();
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
	
	public static FolFormula PlToFo(PropositionalFormula formula) {
		if( formula instanceof Proposition ) {
			return new FOLAtom(new Predicate( ((Proposition)formula).getName() ));
		} else if( formula instanceof net.sf.tweety.logics.pl.syntax.Negation ) {
			net.sf.tweety.logics.pl.syntax.Negation neg;
			neg = (net.sf.tweety.logics.pl.syntax.Negation) formula;
			return new Negation(PlToFo(neg.getFormula()));
		} else if( formula instanceof net.sf.tweety.logics.pl.syntax.Conjunction ) {
			net.sf.tweety.logics.pl.syntax.Conjunction conj;
			conj = (net.sf.tweety.logics.pl.syntax.Conjunction) formula;
			Conjunction result = new Conjunction();
			for(PropositionalFormula inner : conj) {
				result.add(PlToFo(inner));
			}
			return result;
		} else if( formula instanceof net.sf.tweety.logics.pl.syntax.Disjunction ) {
			net.sf.tweety.logics.pl.syntax.Disjunction disj;
			disj = (net.sf.tweety.logics.pl.syntax.Disjunction) formula;
			Disjunction result = new Disjunction();
			for(PropositionalFormula inner : disj) {
				result.add(PlToFo(inner));
			}
			return result;
		} else if( formula instanceof net.sf.tweety.logics.pl.syntax.Tautology ) {
			return new Tautology();
		} else if( formula instanceof net.sf.tweety.logics.pl.syntax.Contradiction ) {
			return new Contradiction();
		}
		return null;
	}
}
