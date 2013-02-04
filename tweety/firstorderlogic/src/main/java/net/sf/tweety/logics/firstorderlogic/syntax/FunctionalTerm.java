package net.sf.tweety.logics.firstorderlogic.syntax;

import java.util.*;

/**
 * A functional term in first-order logic, i.e. a functor and a list
 * of argument terms.
 * @author Matthias Thimm
 */
public class FunctionalTerm extends Term {

	/**
	 * The functor of this atom
	 */
	private Functor functor;
	
	/**
	 * The arguments of the term
	 */
	private List<Term> arguments;
	
	/**
	 * Creates a new functional term with the given functor and the given list
	 * of arguments.
	 * @param functor the functor of this term
	 * @param arguments the list of arguments of this functional term
	 */
	public FunctionalTerm(Functor functor, List<Term> arguments){
		super(functor.getTargetSort());
		this.functor = functor;
		this.arguments = new ArrayList<Term>();
		for(Term t: arguments)
			this.addArgument(t);
	}
	
	/**
	 * Checks whether this term is complete, i.e. whether
	 * every argument is set.
	 * @return "true" if the term is complete.
	 */
	public boolean isComplete(){
		return this.arguments.size() == this.functor.getArity();
	}
	
	/**
	 * Creates a new functional term with the given functor.
	 * @param functor
	 */
	public FunctionalTerm(Functor functor){
		this(functor,new ArrayList<Term>());		
	}
	
	/**
	 * Appends the given argument to this term's
	 * arguments and returns itself.
	 * @param arg an argument to be added
	 * @return the term itself.
	 * @throws IllegalArgumentException if the given term does not correspond
	 *   to the expected sort or the argument list is complete.
	 */
	public FunctionalTerm addArgument(Term term) throws IllegalArgumentException{
		if(this.arguments.size() == this.functor.getArity())
			throw new IllegalArgumentException("No more arguments expected.");
		if(!this.functor.getArguments().get(this.arguments.size()).equals(term.getSort()))
			throw new IllegalArgumentException("The sort \"" + term.getSort() + "\" of the given term does not correspond to the expected sort \"" + this.functor.getArguments().get(this.arguments.size()) + "\"." );
		this.arguments.add(term);		
		return this;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.LogicStructure#getFunctionalTerms()
	 */
	public Set<FunctionalTerm> getFunctionalTerms(){
		Set<FunctionalTerm> terms = new HashSet<FunctionalTerm>();
		terms.add(this);
		for(Term t: this.arguments)
			terms.addAll(t.getFunctionalTerms());
		return terms;		
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.Term#substitute(net.sf.tweety.logics.firstorderlogic.syntax.Term, net.sf.tweety.logics.firstorderlogic.syntax.Term)
	 */
	public Term substitute(Term v, Term t) throws IllegalArgumentException{
		if(!v.getSort().equals(t.getSort()))
			throw new IllegalArgumentException("Cannot replace " + v + " by " + t + " because " + v +
					" is of sort " + v.getSort() + " while " + t + " is of sort " + t.getSort() + ".");
		if(v.equals(this)) return t;
		FunctionalTerm term = new FunctionalTerm(this.functor);
		for(Term s: this.arguments)
			term.addArgument(s.substitute(v, t));
		return term;
	}
	
	/**
	 * Returns the functor of this functional term
	 * @return the functor of this functional term
	 */
	public Functor getFunctor(){
		return this.functor;
	}
	
	/**
	 * Returns the list of arguments of this functional term.
	 * @return the list of arguments of this functional term.
	 */
	public List<Term> getArguments(){
		return new ArrayList<Term>(this.arguments);
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.Term#getConstants()
	 */
	public Set<Constant> getConstants(){
		Set<Constant> constants = new HashSet<Constant>();
		for(Term arg: this.arguments)
			constants.addAll(arg.getConstants());
		return constants;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.Term#getVariables()
	 */
	public Set<Variable> getVariables(){
		Set<Variable> variables = new HashSet<Variable>();
		for(Term arg: this.arguments)
			variables.addAll(arg.getVariables());
		return variables;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.Term#getFunctors()
	 */
	public Set<Functor> getFunctors(){
		Set<Functor> functors = new HashSet<Functor>();
		functors.add(this.functor);
		for(Term arg: this.arguments)
			functors.addAll(arg.getFunctors());
		return functors;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.Term#toString()
	 */
	public String toString(){
		String output = this.functor.getName();
		if(this.arguments.size() == 0) return output;
		output += "(";
		output += this.arguments.get(0);
		for(int i = 1; i < arguments.size(); i++)
			output += ","+arguments.get(i);
		output += ")";
		return output;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((arguments == null) ? 0 : arguments.hashCode());
		result = prime * result + ((functor == null) ? 0 : functor.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		FunctionalTerm other = (FunctionalTerm) obj;
		if (arguments == null) {
			if (other.arguments != null)
				return false;
		} else if (!arguments.equals(other.arguments))
			return false;
		if (functor == null) {
			if (other.functor != null)
				return false;
		} else if (!functor.equals(other.functor))
			return false;
		return true;
	}

	@Override
	public String getName() {
		return this.functor.getName();
	}

	@Override
	public boolean isVariable() {
		return false;
	}

	@Override
	public boolean isConstant() {
		return false;
	}

	@Override
	public boolean isNumber() {
		return false;
	}
}
