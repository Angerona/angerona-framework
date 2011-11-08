package net.sf.tweety.logics.firstorderlogic.syntax;

import java.util.*;

import net.sf.tweety.*;
import net.sf.tweety.util.SetTools;


/**
 * The common abstract class for formulas of first-order logic.
 * 
 * NOTE: "RelationalFormula" and "FolFormula" differ in their meaning as follows:
 * <ul>
 * 	<li>A relational formula is any formula over a first-order signature, i.e. even a conditional</li>
 *  <li>A first-order formula is the actual first-order formula in the classical sense.</li>
 * </ul>
 * @author Matthias Thimm
  */
public abstract class FolFormula extends RelationalFormula{	
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.kr.ClassicalFormula#combineWithAnd(net.sf.tweety.kr.Formula)
	 */
	public FolFormula combineWithAnd(ClassicalFormula f){
		if(!(f instanceof FolFormula))
			throw new IllegalArgumentException("The given formula " + f + " is not a first-order formula.");
		return new Conjunction(this,(FolFormula)f);
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.kr.ClassicalFormula#combineWithOr(net.sf.tweety.kr.ClassicalFormula)
	 */
	public FolFormula combineWithOr(ClassicalFormula f){
		if(!(f instanceof FolFormula))
			throw new IllegalArgumentException("The given formula " + f + " is not a first-order formula.");
		return new Disjunction(this,(FolFormula)f);
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.kr.ClassicalFormula#complement()
	 */
	public FolFormula complement(){
		if(this instanceof Negation) return ((Negation)this).getFormula();
		return new Negation(this);
	}	
	
	/**
	 * Makes a disjunctive normal form of this formula.
	 * @return the DNF of this formula
	 */
	public FolFormula toDnf(){
		if(this.isDnf()) return this;
		if(this.containsQuantifier()) throw new UnsupportedOperationException("Cannot convert quantified formula into DNF.");
		FolFormula nnf = this.toNnf();

    // DNF( P || Q) = DNF(P) || DNF(Q)
    if(nnf instanceof Disjunction) {
      Disjunction d = (Disjunction) nnf;
      Disjunction dnf = new Disjunction();
      for(RelationalFormula f : d) {
        if(f instanceof FolFormula)
          dnf.add( ((FolFormula)f).toDnf() );
        else throw new IllegalStateException("Can not convert disjunctions containing non-first-order formulae to NNF.");
      }
      return dnf.collapseAssociativeFormulas();
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
      Set<Set<RelationalFormula>> disjunctions = new HashSet<Set<RelationalFormula>>();
      for(RelationalFormula f : c) {
        if(! (f instanceof FolFormula)) throw new IllegalStateException("Can not convert conjunctions containing non-first-order formulae to NNF.");
        FolFormula fdnf = ((FolFormula)f).toDnf();
        Set<RelationalFormula> elems = new HashSet<RelationalFormula>();
        if(fdnf instanceof Disjunction) {
          elems.addAll( (Disjunction)fdnf );
        } else {
          elems.add( fdnf );
        }
        disjunctions.add( elems );
      }
      
     // the dnf is the disjunction of all possible combinations of distributed conjunctions
      Set<Set<RelationalFormula>> permutations = new SetTools< RelationalFormula >().permutations( disjunctions );
      Disjunction dnf = new Disjunction();
      for(Set<RelationalFormula> elems : permutations) {
        dnf.add( new Conjunction( elems ) );
      }
      return dnf.collapseAssociativeFormulas();
    }
    return nnf;
	}
	
	/**
	 * Makes the negation normal form of this formula.
	 * @return the NNF of this formula
	 */
	public abstract FolFormula toNnf();
	 
  /**
   * This method collapses all associative operations appearing
   * in this term, e.g. every a||(b||c) becomes a||b||c.
   * @return the collapsed formula.
   */
  public abstract FolFormula collapseAssociativeFormulas();
  
	
	/**
	 * Returns all quantified formulas appearing in this formula.
	 * @return the set of all quantified formulas appearing in this formula.
	 */
	public abstract Set<QuantifiedFormula> getQuantifiedFormulas();
	
	/**
	 * Returns all disjunctions appearing in this formula.
	 * @return the set of all disjunctions appearing in this formula.
	 */
	public abstract Set<Disjunction> getDisjunctions();
	
	/**
	 * Returns all conjunctions appearing in this formula.
	 * @return the set of all conjunctions appearing in this formula.
	 */
	public abstract Set<Conjunction> getConjunctions();
	
	/**
	 * Checks whether this formula is in disjunctive normal form.
	 * @return "true" iff this formula is in disjunctive normal form.
	 */
	public abstract boolean isDnf();
	
	/**
	 * Checks whether this formula is a literal, i.e.
	 * whether it is an atom or a negated atom.
	 * @return "true" iff this formula is a literal.
	 */
	public abstract boolean isLiteral();
}
