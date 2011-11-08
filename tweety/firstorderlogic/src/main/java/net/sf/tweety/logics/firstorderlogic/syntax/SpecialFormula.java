package net.sf.tweety.logics.firstorderlogic.syntax;

import java.util.*;

/**
 * This class captures the common functionalities of the special
 * formulas tautology and contradiction.
 * @author Matthias Thimm
 */
public abstract class SpecialFormula extends FolFormula{
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#containsQuantifier()
	 */
	@Override
	public boolean containsQuantifier() {
		return false;
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#getAtoms()
	 */
	@Override
	public Set<Atom> getAtoms() {
		return new HashSet<Atom>();
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#getPredicates()
	 */
	@Override
	public Set<Predicate> getPredicates() {
		return new HashSet<Predicate>();
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#isClosed()
	 */
	@Override
	public boolean isClosed() {
		return true;
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#isClosed(java.util.Set)
	 */
	@Override
	public boolean isClosed(Set<Variable> boundVariables) {
		return true;
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#isWellBound()
	 */
	@Override
	public boolean isWellBound() {
		return true;
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#isWellBound(java.util.Set)
	 */
	@Override
	public boolean isWellBound(Set<Variable> boundVariables) {
		return true;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#isLiteral()
	 */
	public boolean isLiteral(){
		//TODO is this correct?
		return true;
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.RelationalFormula#substitute(net.sf.tweety.logics.firstorderlogic.syntax.Term, net.sf.tweety.logics.firstorderlogic.syntax.Term)
	 */
	@Override
	public FolFormula substitute(Term v, Term t){
		return this;
	}
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.LogicStructure#getConstants()
	 */
	@Override
	public Set<Constant> getConstants() {
		return new HashSet<Constant>();
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.LogicStructure#getFunctionalTerms()
	 */
	@Override
	public Set<FunctionalTerm> getFunctionalTerms() {
		return new HashSet<FunctionalTerm>();
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#getUnboundVariables()
	 */
	public Set<Variable> getUnboundVariables(){
		return this.getVariables();
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.LogicStructure#getFunctors()
	 */
	@Override
	public Set<Functor> getFunctors() {
		return new HashSet<Functor>();
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.LogicStructure#getVariables()
	 */
	@Override
	public Set<Variable> getVariables() {
		return new HashSet<Variable>();
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#getQuantifiedFormulas()
	 */
	public Set<QuantifiedFormula> getQuantifiedFormulas(){
		return new HashSet<QuantifiedFormula>();
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#getDisjunctions()
	 */
	public Set<Disjunction> getDisjunctions(){
		return new HashSet<Disjunction>();
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#getConjunctions()
	 */
	public Set<Conjunction> getConjunctions(){
		return new HashSet<Conjunction>();
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#isDnf()
	 */
	public boolean isDnf(){
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#toNNF()
	 */
	@Override
	public FolFormula toNnf() {
	  return this;
	}

	/*
	 * (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#collapseAssociativeFormulas()
	 */
	@Override
	public FolFormula collapseAssociativeFormulas() {
	  return this;
	}
}
