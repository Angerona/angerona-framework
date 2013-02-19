package net.sf.tweety.logicprogramming.asplibrary.syntax;

import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.Signature;

public class ElpSignature extends Signature {
	
	private Set<Predicate> predicates;
	
	private Set<Constant> constants;
	
	public ElpSignature() {
		predicates = new HashSet<Predicate>();
		constants = new HashSet<Constant>();
	}
	
	public void add(Object obj) {
		if(obj == null)	return;
		if(obj instanceof Neg) {
			obj = ((Neg)obj).getAtom();
		}
		
		if(obj instanceof Atom) {
			Atom a = (Atom)obj;
			predicates.add(a.getPredicate());
			
			for(Term<?> t : a.getTerms()) {
				if(t instanceof Constant) {
					constants.add((Constant)t);
				}
			}
		}
	}

	@Override
	public boolean isSubSignature(Signature other) {
		if(other instanceof ElpSignature) {
			
		}
		return false;
	}

	@Override
	public void addSignature(Signature other) {
		if(other instanceof ElpSignature) {
			
		}
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return false;
	}
}
