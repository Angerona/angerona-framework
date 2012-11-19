package net.sf.tweety.logics.propositionallogic.semantics;

import java.util.*;

import net.sf.tweety.*;
import net.sf.tweety.logics.propositionallogic.*;
import net.sf.tweety.logics.propositionallogic.syntax.*;
import net.sf.tweety.util.*;


/**
 * This class represents a possible world of propositional logic, i.e.
 * some set of propositions.
 * 
 * @author Matthias Thimm
 */
public class PossibleWorld extends InterpretationSet<Proposition> {
	
	/**
	 * Creates a new empty possible world.
	 */
	public PossibleWorld(){
		this(new HashSet<Proposition>());
	}
	
	/**
	 * Creates a new possible world with the given set of propositions.
	 * @param propositions the propositions that are true in this possible world
	 */
	public PossibleWorld(Collection<? extends Proposition> propositions){
		super(propositions);
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.kr.Interpretation#satisfies(net.sf.tweety.kr.Formula)
	 */
	@Override
	public boolean satisfies(Formula formula) throws IllegalArgumentException {
		if(!(formula instanceof PropositionalFormula))
			 throw new IllegalArgumentException("Formula " + formula + " is not a propositional formula.");
		if(formula instanceof Contradiction)
			return false;
		if(formula instanceof Tautology)
			return true;
		if(formula instanceof Proposition)
			return this.contains(formula);
		if(formula instanceof Negation)
			return !this.satisfies(((Negation)formula).getFormula());
		if(formula instanceof Conjunction){
			Conjunction c = (Conjunction) formula;
			for(PropositionalFormula f : c)
				if(!this.satisfies(f))
					return false;
			return true;
		}
		if(formula instanceof Disjunction){
			Disjunction d = (Disjunction) formula;
			for(PropositionalFormula f: d)
				if(this.satisfies(f))
					return true;
			return false;
		}
		throw new IllegalArgumentException("Propositional formula " + formula + " is of unknown type.");
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.kr.Interpretation#satisfies(net.sf.tweety.kr.BeliefBase)
	 */
	@Override
	public boolean satisfies(BeliefBase beliefBase) throws IllegalArgumentException {
		if(!(beliefBase instanceof PlBeliefSet))
			throw new IllegalArgumentException("Propositional knowledge base expected.");
		PlBeliefSet pKb = (PlBeliefSet) beliefBase;
		for(Formula f: pKb)
			if(!this.satisfies(f))
				return false;
		return true;
	}
	
	/**
	 * Returns the set of all possible worlds for the
	 * given propositional signature.
	 * @param signature a propositional signature.
	 * @return the set of all possible worlds for the
	 * given propositional signature.
	 */
	public static Set<PossibleWorld> getAllPossibleWorlds(PropositionalSignature signature){
		Set<PossibleWorld> possibleWorlds = new HashSet<PossibleWorld>();
		Set<Set<Proposition>> propositions = new SetTools<Proposition>().subsets(signature);
		for(Set<Proposition> p: propositions)
			possibleWorlds.add(new PossibleWorld(p));
		return possibleWorlds;
	}

}
