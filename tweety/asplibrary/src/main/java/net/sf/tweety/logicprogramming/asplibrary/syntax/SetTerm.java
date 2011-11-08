package net.sf.tweety.logicprogramming.asplibrary.syntax;

import java.util.*;

/**
 * this class models a set term, which can be used
 * for sets in dlv complex programs.
 * 
 * @author Thomas Vengels
 *
 */
public class SetTerm implements Term {

	Set<Term>	terms;
	
	public SetTerm(Collection<Term> terms) {
		this.terms = new HashSet<Term>(terms);
	}
	
	@Override
	public boolean isConstant() {
		return false;
	}

	@Override
	public boolean isVariable() {
		return false;
	}

	@Override
	public boolean isAtom() {
		return false;
	}

	@Override
	public boolean isList() {
		return false;
	}

	@Override
	public boolean isSet() {
		return true;
	}

	@Override
	public boolean isNumber() {
		return false;
	}

	@Override
	public void set(String value) {
		// not supported
	}

	@Override
	public String get() {
		// not supported
		return null;
	}

	@Override
	public void set(int value) {
		// not supported
	}

	@Override
	public int getInt() {
		// not supported
		return 0;
	}

	@Override
	public String toString() {
		String ret = "{";
		Iterator<Term> iter = this.terms.iterator();
		if (iter.hasNext())
			ret += iter.next();
		while (iter.hasNext())
			ret += ", "+iter.next();
		ret +="}";
		return ret;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof SetTerm) {
			SetTerm os = (SetTerm) o;
			
			// both sets must be same size, and
			// every element from here should be
			// in there.
			for (Term t : this.terms)
				if (!os.terms.contains(t))
					return false;
			
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean isString() {
		return false;
	}
}
