package net.sf.tweety.logicprogramming.asplibrary.syntax;

import java.util.*;

/**
 * this class models a set term, which can be used
 * for sets in dlv complex programs.
 * 
 * @author Tim Janus
 * @author Thomas Vengels
 *
 */
public class SetTerm implements Term<Set<Term<?>>> {

	Set<Term<?>>	terms;
	
	public SetTerm(Collection<Term<?>> terms) {
		this.terms = new HashSet<Term<?>>(terms);
	}
	
	
	@Override
	public void set(Set<Term<?>> value) {
		terms = value;
	}

	@Override
	public Set<Term<?>> get() {
		return terms;
	}

	@Override
	public String toString() {
		String ret = "{";
		Iterator<Term<?>> iter = this.terms.iterator();
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
			for (Term<?> t : this.terms)
				if (!os.terms.contains(t))
					return false;
			
			return true;
		} else {
			return false;
		}
	}
}
