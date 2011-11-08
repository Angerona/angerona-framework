package net.sf.tweety.logics.firstorderlogic.syntax;

import java.util.*;

/**
 * The common parent of exists and forall quantified formulas, which contains common
 * functionalities.
 * @author Matthias Thimm
 */
public abstract class QuantifiedFormula extends FolFormula{
	
	/**
	 * The folFormula this quantified folFormula ranges over. 
	 */
	private FolFormula folFormula;
	
	/**
	 * The variables of this quantified folFormula.
	 */
	private Set<Variable> quantifier_variables;
		
	/**
	 * Creates a new quantified folFormula with the given folFormula and variables.
	 * @param folFormula the folFormula this quantified folFormula ranges over.
	 * @param variables the variables of this quantified folFormula.
	 */
	public QuantifiedFormula(RelationalFormula folFormula, Set<Variable> variables){
		if(!(folFormula instanceof FolFormula))
			throw new IllegalArgumentException("Formula must be first-order formula.");
		this.folFormula = (FolFormula)folFormula;
		this.quantifier_variables = new HashSet<Variable>(variables);
		if(!this.isWellFormed()) throw new IllegalArgumentException("FolFormula not well-formed.");
	}
	
	/**
	 * Creates a new quantified folFormula with the given folFormula and variable.
	 * @param folFormula the folFormula this quantified folFormula ranges over.
	 * @param quantifier_variables the variable of this quantified folFormula.
	 */
	public QuantifiedFormula(FolFormula folFormula, Variable variable){
		Set<Variable> variables = new HashSet<Variable>();
		variables.add(variable);
		this.folFormula = folFormula;
		this.quantifier_variables = variables;
		if(!this.isWellFormed()) throw new IllegalArgumentException("FolFormula not well-formed.");
	}
		
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#isClosed()
	 */
	public boolean isClosed(){		
		return this.folFormula.isClosed(this.quantifier_variables);		
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#isClosed(java.util.Set)
	 */
	public boolean isClosed(Set<Variable> boundVariables){
		Set<Variable> variables = new HashSet<Variable>(this.quantifier_variables);
		variables.addAll(boundVariables);
		return this.folFormula.isClosed(variables);
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#containsQuantifier()
	 */
	public boolean containsQuantifier(){
		return true;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#isWellBound()
	 */
	public boolean isWellBound(){
		return this.folFormula.isWellBound(this.quantifier_variables);
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#isWellBound(java.util.Set)
	 */
	public boolean isWellBound(Set<Variable> boundVariables){
		Set<Variable> intersection = new HashSet<Variable>(this.quantifier_variables);
		intersection.retainAll(boundVariables);		
		if(!intersection.isEmpty()) return false;		
		Set<Variable> variables = new HashSet<Variable>(this.quantifier_variables);
		variables.addAll(boundVariables);		
		return this.folFormula.isWellBound(variables);
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#getConstants()
	 */
	public Set<Constant> getConstants(){
		return this.folFormula.getConstants();
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#getPredicates()
	 */
	public Set<Predicate> getPredicates(){
		return this.folFormula.getPredicates();
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#getFunctors()
	 */
	public Set<Functor> getFunctors(){
		return this.folFormula.getFunctors();
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#getAtoms()
	 */
	public Set<Atom> getAtoms(){
		return this.folFormula.getAtoms();
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#getUnboundVariables()
	 */
	public Set<Variable> getUnboundVariables(){
		Set<Variable> variables = this.getVariables();
		variables.removeAll(this.quantifier_variables);
		return variables;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#getFunctionalTerms()
	 */
	public Set<FunctionalTerm> getFunctionalTerms(){
		return this.folFormula.getFunctionalTerms();
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#getVariables()
	 */
	public Set<Variable> getVariables(){
		Set<Variable> variables = new HashSet<Variable>();
		variables.addAll(this.quantifier_variables);
		variables.addAll(folFormula.getVariables());
		return variables;
	}
	
	/**
	 * Returns the folFormula this quantified folFormula ranges over
	 * @return the folFormula this quantified folFormula ranges over
	 */
	public FolFormula getFormula(){
		return this.folFormula;
	}
	
	/**
	 * Returns the variables of this quantified folFormula.
	 * @return the variables of this quantified folFormula.
	 */
	public Set<Variable> getQuantifierVariables(){
		return new HashSet<Variable>(this.quantifier_variables);
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#getQuantifiedFormulas()
	 */
	public Set<QuantifiedFormula> getQuantifiedFormulas(){
		Set<QuantifiedFormula> qf = new HashSet<QuantifiedFormula>();
		qf.addAll(this.folFormula.getQuantifiedFormulas());
		qf.add(this);
		return qf;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#getDisjunctions()
	 */
	public Set<Disjunction> getDisjunctions(){
		return this.folFormula.getDisjunctions();
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#getConjunctions()
	 */
	public Set<Conjunction> getConjunctions(){
		return this.folFormula.getConjunctions();
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#isDnf()
	 */
	public boolean isDnf(){
		return false;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#isLiteral()
	 */
	public boolean isLiteral(){
		return false;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((folFormula == null) ? 0 : folFormula.hashCode());
		result = prime * result
				+ ((quantifier_variables == null) ? 0 : quantifier_variables.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		QuantifiedFormula other = (QuantifiedFormula) obj;
		if (folFormula == null) {
			if (other.folFormula != null)
				return false;
		} else if (!folFormula.equals(other.folFormula))
			return false;
		if (quantifier_variables == null) {
			if (other.quantifier_variables != null)
				return false;
		} else if (!quantifier_variables.equals(other.quantifier_variables))
			return false;
		return true;
	}
}
