package net.sf.tweety.logics.firstorderlogic.syntax;

import java.util.*;

/**
 * A constant of first-order logic, i.e. a term that names a specific object.
 * @author Matthias Thimm
 */
public class Constant extends Term {	
	
	private String name;
	
	public Constant(String name){
		this(name,Sort.THING);
	}
	
	public Constant(String name, Sort sort){
		super(sort);
		this.name = name;	
		sort.add(this);
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.Term#getConstants()
	 */
	public Set<Constant> getConstants(){
		Set<Constant> constants = new HashSet<Constant>();
		constants.add(this);
		return constants;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.Term#getFunctors()
	 */
	public Set<Functor> getFunctors(){
		return new HashSet<Functor>();
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.Term#getVariables()
	 */
	public Set<Variable> getVariables(){
		return new HashSet<Variable>();
	}	

	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.Term#substitute(net.sf.tweety.logics.firstorderlogic.syntax.Term, net.sf.tweety.logics.firstorderlogic.syntax.Term)
	 */
	public Term substitute(Term v, Term t) throws IllegalArgumentException{		
		if(!v.getSort().equals(t.getSort()))
			throw new IllegalArgumentException("Cannot replace " + v + " by " + t + " because " + v +
					" is of sort " + v.getSort() + " while " + t + " is of sort " + t.getSort() + ".");
		if(v.equals(this)) return t;
		return this;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.LogicStructure#getFunctionalTerms()
	 */
	public Set<FunctionalTerm> getFunctionalTerms(){
		return new HashSet<FunctionalTerm>();
	}
	
	/**
	 * Returns the name of this constant
	 * @return the name of this constant
	 */
	public String getName(){
		return this.name;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.Term#toString()
	 */
	public String toString(){
		return this.name;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Constant other = (Constant) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if(!other.getSort().equals(this.getSort()))
			return false;
		return true;
	}

	@Override
	public boolean isVariable() {
		return false;
	}

	@Override
	public boolean isConstant() {
		return true;
	}

	@Override
	public boolean isNumber() {
		return false;
	}
	
}
