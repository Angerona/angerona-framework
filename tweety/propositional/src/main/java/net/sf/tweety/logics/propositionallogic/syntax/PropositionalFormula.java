package net.sf.tweety.logics.propositionallogic.syntax;

import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.Signature;
import net.sf.tweety.logics.commons.ClassicalFormula;
import net.sf.tweety.logics.propositionallogic.semantics.PossibleWorld;
import net.sf.tweety.math.probability.Probability;
import net.sf.tweety.util.SetTools;

/**
 * This class represents the common ancestor for propositional formulae.
 * 
 * @author Matthias Thimm
 */
public abstract class PropositionalFormula implements ClassicalFormula {

	/* (non-Javadoc)
	 * @see net.sf.tweety.kr.Formula#getSignature()
	 */
	@Override
	public Signature getSignature() {
		return new PropositionalSignature(this.getPropositions());
	}
	
	/**
	 * Returns the set of propositions that appear in this formula.
	 * @return the set of propositions that appear in this formula.
	 */
	public abstract Set<Proposition> getPropositions();
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.kr.ClassicalFormula#combineWithAnd(net.sf.tweety.kr.ClassicalFormula)
	 */
	public ClassicalFormula combineWithAnd(ClassicalFormula f){
		if(!(f instanceof PropositionalFormula))
			throw new IllegalArgumentException("The given formula " + f + " is not a propositional formula.");
		return new Conjunction(this,(PropositionalFormula)f);
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.kr.ClassicalFormula#combineWithOr(net.sf.tweety.kr.ClassicalFormula)
	 */
	public ClassicalFormula combineWithOr(ClassicalFormula f){
		if(!(f instanceof PropositionalFormula))
			throw new IllegalArgumentException("The given formula " + f + " is not a propositional formula.");
		return new Disjunction(this,(PropositionalFormula)f);
	}
	
	/**
	 * This method collapses all associative operations appearing
	 * in this term, e.g. every a||(b||c) becomes a||b||c.
	 * @return the collapsed formula.
	 */
	public abstract PropositionalFormula collapseAssociativeFormulas();
	
	/**
	 * Returns this formula's probability in the uniform distribution. 
	 * @return this formula's probability in the uniform distribution.
	 */
	public Probability getUniformProbability(){
		Set<PossibleWorld> worlds = PossibleWorld.getAllPossibleWorlds((PropositionalSignature)this.getSignature());
		int cnt = 0;
		for(PossibleWorld world: worlds)
			if(world.satisfies(this))
				cnt++;
		return new Probability(new Double(cnt)/new Double(worlds.size()));
	}
	
    /**
     * This method returns this formula in negation normal form (NNF).
     * A formula is in NNF iff negations occur only directly in front of a proposition.
     * @return the formula in NNF.
     */
	public abstract PropositionalFormula toNnf();
	
    /**
	 * This method returns this formula in disjunctive normal form (DNF).
	 * A formula is in DNF iff it is a disjunction of conjunctive clauses.
	 * @return the formula in DNF.
	 */
	public PropositionalFormula toDnf(){
		PropositionalFormula nnf = this.toNnf();
	    // DNF( P || Q) = DNF(P) || DNF(Q)
	    if(nnf instanceof Disjunction) {
	      Disjunction d = (Disjunction) nnf;
	      Disjunction dnf = new Disjunction();
	      for(PropositionalFormula f : d) {
	        dnf.add( f.toDnf() );
	      }
	    return dnf;
	}
    
    /* DNF( P_1 && P_2 && ... && P_k) is calculated as follows:
     * 1. DNF(P_1) = P_11 || P_12 || ... || P_1l
     *    DNF(P_2) = P_21 || P_22 || ... || P_2m
     *    ...
     *    DNF(P_k) = P_k1 || P_k2 || ... || P_kn
     *    each P_ij is a conjunction of literals (propositions or negations of propositions)
     * 2. the dnf is then the disjunction of all possible permutations of distributed conjunctions of P_ij, eg:
     *    DNF(P) = p1 || p2 || p3
     *    DNF(Q) = q1 || q2
     *    DNF(R) = r1
     *    DNF(P && Q && R) = (p1 && q1 && r1) || (p1 && q2 && r1) || 
     *                       (p2 && q1 && r1) || (p2 && q2 && r1) || 
     *                       (p3 && q1 && r1) || (p3 && q2 && r1)
     */
    if(nnf instanceof Conjunction) {
      Conjunction c = (Conjunction) nnf;
      Set<Set<PropositionalFormula>> disjunctions = new HashSet<Set<PropositionalFormula>>();
      for(PropositionalFormula f : c) {
        PropositionalFormula fdnf = f.toDnf().collapseAssociativeFormulas();
        Set<PropositionalFormula> elems = new HashSet<PropositionalFormula>();
        disjunctions.add( elems );
        if(fdnf instanceof Disjunction) {
          elems.addAll( (Disjunction)fdnf );
        } else {
          elems.add( fdnf );
        }
      }
      
     // the dnf is the disjunction of all possible combinations of distributed conjunctions
      Set<Set<PropositionalFormula>> permutations = new SetTools< PropositionalFormula >().permutations( disjunctions );
      Disjunction dnf = new Disjunction();
      for(Set<PropositionalFormula> elems : permutations) {
        dnf.add( new Conjunction( elems ) );
      }
      return dnf;
    }
    return nnf;
  }
  
	/* (non-Javadoc)
	 * @see net.sf.tweety.kr.ClassicalFormula#complement()
	 */
	public ClassicalFormula complement(){
		if(this instanceof Negation)
			return ((Negation)this).getFormula();
		return new Negation(this);
	}

}
