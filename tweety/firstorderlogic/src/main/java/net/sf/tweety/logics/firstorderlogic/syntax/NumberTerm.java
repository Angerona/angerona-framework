package net.sf.tweety.logics.firstorderlogic.syntax;

import java.util.HashSet;
import java.util.Set;

public class NumberTerm extends Term {

	private int number;
	
	public NumberTerm(int number) {
		super(Sort.THING);
		this.number = number;
	}
	
	public NumberTerm(int number, Sort sort) {
		super(sort);
		this.number = number;
	}
	
	public int getNumber() {
		return number;
	}
	
	@Override
	public String getName() {
		return String.valueOf(number);
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

	@Override
	public Term substitute(Term v, Term t) throws IllegalArgumentException {
		if(!v.getSort().equals(t.getSort()))
			throw new IllegalArgumentException("Cannot replace " + v + " by " + t + " because " + v +
					" is of sort " + v.getSort() + " while " + t + " is of sort " + t.getSort() + ".");
		if(v.equals(this)) return t;
		return this;
	}

	@Override
	public Set<Constant> getConstants() {
		return new HashSet<Constant>();
	}

	@Override
	public Set<Functor> getFunctors() {
		return new HashSet<Functor>();
	}

	@Override
	public Set<Variable> getVariables() {
		return new HashSet<Variable>();
	}

	@Override
	public Set<FunctionalTerm> getFunctionalTerms() {
		return new HashSet<FunctionalTerm>();
	}

	@Override
	public String toString() {
		return String.valueOf(number);
	}

}
