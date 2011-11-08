package net.sf.tweety.logics.firstorderlogic.syntax;

import java.util.*;

/**
 * An atom in first-order logic, i.e. a predicate and a list of argument terms.
 * @author Matthias Thimm
 */
public class Atom extends FolFormula {

	/**
	 * The predicate of this atom
	 */
	private Predicate predicate;
	
	/**
	 * The arguments of the atom
	 */
	private List<Term> arguments;
	
	/**
	 * Creates a new atom with the given predicate and initializes
	 * an empty argument list.
	 * @param predicate the predicate of the atom.
	 */
	public Atom(Predicate predicate){
		this(predicate,new ArrayList<Term>());
	}
	
	/**
	 * Creates a new atom with the given predicate and list of
	 * terms
	 * @param predicate the predicate of the atom
	 * @param arguments the arguments (terms) of the atom
	 */
	public Atom(Predicate predicate, List<? extends Term> arguments){
		this.predicate = predicate;
		this.arguments = new ArrayList<Term>();
		for(Term t: arguments)
			this.addArgument(t);		
	}
	
	/**
	 * Appends the given argument to this atom's
	 * arguments and returns itself.
	 * @param arg an argument to be added
	 * @return the atom itself.
	 * @throws IllegalArgumentException if the given term does not correspond
	 *   to the expected sort or the argument list is complete.
	 */
	public Atom addArgument(Term term) throws IllegalArgumentException{
		if(this.arguments.size() == this.predicate.getArity())
			throw new IllegalArgumentException("No more arguments expected.");
		if(!this.predicate.getArguments().get(this.arguments.size()).equals(term.getSort()))
			throw new IllegalArgumentException("The sort \"" + term.getSort() + "\" of the given term does not correspond to the expected sort \"" + this.predicate.getArguments().get(this.arguments.size()) + "\"." );
		this.arguments.add(term);		
		return this;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.RelationalFormula#substitute(net.sf.tweety.logics.firstorderlogic.syntax.Term, net.sf.tweety.logics.firstorderlogic.syntax.Term)
	 */
	public Atom substitute(Term v, Term t) throws IllegalArgumentException{
		Atom atom = new Atom(this.predicate);
		for(Term term: this.arguments)
			atom.addArgument(term.substitute(v, t));
		return atom;		
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#getAtoms()
	 */
	public Set<Atom> getAtoms(){
		HashSet<Atom> atoms = new HashSet<Atom>();
		atoms.add(this);
		return atoms;		
	}
	
	/**
	 * Checks whether this atom is complete, i.e. whether
	 * every argument is set.
	 * @return "true" if the atom is complete.
	 */
	public boolean isComplete(){
		return this.arguments.size() == this.predicate.getArity();
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#getUnboundVariables()
	 */
	public Set<Variable> getUnboundVariables(){
		return this.getVariables();
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.LogicStructure#getConstants()
	 */
	public Set<Constant> getConstants(){
		Set<Constant> constants = new HashSet<Constant>();
		for(Term arg: arguments)
			constants.addAll(arg.getConstants());
		return constants;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#getPredicates()
	 */
	public Set<Predicate> getPredicates(){
		Set<Predicate> predicates = new HashSet<Predicate>();
		predicates.add(this.predicate);
		return predicates;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.LogicStructure#getFunctors()
	 */
	public Set<Functor> getFunctors(){
		Set<Functor> functors = new HashSet<Functor>();
		for(Term arg: this.arguments)
			functors.addAll(arg.getFunctors());
		return functors;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.LogicStructure#getVariables()
	 */
	public Set<Variable> getVariables(){
		Set<Variable> variables = new HashSet<Variable>();
		for(Term arg: this.arguments)
			variables.addAll(arg.getVariables());
		return variables;
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.LogicStructure#getFunctionalTerms()
	 */
	public Set<FunctionalTerm> getFunctionalTerms(){
		Set<FunctionalTerm> terms = new HashSet<FunctionalTerm>();
		for(Term t: this.arguments)
			if(t instanceof FunctionalTerm)
				terms.add((FunctionalTerm)t);
		return terms;		
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#isClosed()
	 */
	public boolean isClosed(){
		return this.getVariables().isEmpty();
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#isClosed(java.util.Set)
	 */
	public boolean isClosed(Set<Variable> boundVariables){
		return boundVariables.containsAll(this.getVariables());
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#isWellBound()
	 */
	public boolean isWellBound(){
		return true;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#isWellBound(java.util.Set)
	 */
	public boolean isWellBound(Set<Variable> boundVariables){
		return true;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#containsQuantifier()
	 */
	public boolean containsQuantifier(){
		return false;
	}
	
	/**
	 * Returns the predicate of this atom
	 * @return the predicate of this atom
	 */
	public Predicate getPredicate(){
		return this.predicate;
	}
	
	/**
	 * Returns the arguments of this atom.
	 * @return the arguments of this atom.
	 */
	public List<Term> getArguments(){
		return new ArrayList<Term>(this.arguments);
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
	public Set<Conjunction> getConjunctions() {
		return new HashSet<Conjunction>();
	}

	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#getQuantifiedFormulas()
	 */
	public Set<QuantifiedFormula> getQuantifiedFormulas() {
		return new HashSet<QuantifiedFormula>();
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#isDnf()
	 */
	public boolean isDnf(){
		return true;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#isLiteral()
	 */
	public boolean isLiteral(){
		return true;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#toString()
	 */
	public String toString(){
		if(!this.isWellFormed()) throw new IllegalArgumentException("FolFormula not well-formed.");
		String output = this.predicate.getName();
		if(this.arguments.size() == 0) return output;
		output += "(";
		output += this.arguments.get(0);
		for(int i = 1; i < arguments.size(); i++)
			output += ","+arguments.get(i);
		output += ")";
		return output;
	}
	
	/*
	 * (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolFormula#toNnf()
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

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((arguments == null) ? 0 : arguments.hashCode());
		result = prime * result
				+ ((predicate == null) ? 0 : predicate.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;		
		if (getClass() != obj.getClass())
			return false;
		Atom other = (Atom) obj;
		if(!other.isWellFormed()) throw new IllegalArgumentException("FolFormula not well-formed.");
		if (arguments == null) {
			if (other.arguments != null)
				return false;
		} else if (!arguments.equals(other.arguments))
			return false;
		if (predicate == null) {
			if (other.predicate != null)
				return false;
		} else if (!predicate.equals(other.predicate))
			return false;
		return true;
	}
}
