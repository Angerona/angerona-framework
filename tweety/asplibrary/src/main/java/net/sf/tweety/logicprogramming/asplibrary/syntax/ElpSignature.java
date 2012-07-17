package net.sf.tweety.logicprogramming.asplibrary.syntax;

import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.Signature;
import net.sf.tweety.SymbolSet;
import net.sf.tweety.SymbolSetAdapter;
import net.sf.tweety.logics.CommonStructure;
import net.sf.tweety.logics.CommonTerm;

public class ElpSignature extends Signature {

	// functional test
	public static void main(String [] args ) {
		Program p = new Program();
		Rule r = new Rule();
		r.addHead(new Atom("attend_scm"));
		r.addBody(new Atom("who_argued", new Constant("john")));
		p.add(r);

		SymbolSet ss = p.getSignature().getSymbolSet();
		System.out.println(ss);
	}
	
	private class SymbolSetElp extends SymbolSetAdapter {

		public SymbolSetElp(boolean sorted) {
			super(sorted);
		}

		@Override
		public void fromSignature(Signature signature) {
			ElpSignature sig = (ElpSignature)signature;
			for(CommonStructure cs : sig.getCommonStructureElements()) {
				if(cs.isPredicate()) {
					this.symbols.add(cs.getName());
					this.aritys.put(cs.getName(), cs.getArity());
				}
			}
			
			for(String constant : sig.constants) {
				this.constants.add(constant);
			}
		}
		
	}
	
	private Set<CommonStructure> predicates;
	
	private Set<String> constants;
	
	public ElpSignature() {
		predicates = new HashSet<CommonStructure>();
		constants = new HashSet<String>();
	}
	
	public void add(Object obj) {
		if(obj == null)	return;
		if(obj instanceof Neg) {
			obj = ((Neg)obj).getAtom();
		}
		
		if(obj instanceof Atom) {
			if(!predicates.contains(obj))
				predicates.add((CommonStructure)obj);
			
			Atom a = (Atom)obj;
			//System.out.println(a.toString() +":");
			for(int i=0; i<a.getArity(); i++) {
				Term t = a.getTerm(i);
				if(t.isConstant()) {
					if(!constants.contains(t.get())) {
						constants.add(t.get());
					}
				} else {
					/*
					System.out.println(t.get()+":");
					System.out.println("Is String: " + t.isString());
					System.out.println("Is Atom: " + t.isAtom());
					System.out.println("Is Variable: " + t.isVariable());
					System.out.println("Is List: " + t.isList());
					System.out.println("");
					*/
				}
			}
		}
		
	}
	
	@Override
	public void fromSymbolSet(SymbolSet symbols) {
		throw new RuntimeException("Not Implemented yet");
	}

	@Override
	public SymbolSet getSymbolSet() {
		SymbolSetAdapter reval = new SymbolSetElp(false);
		reval.fromSignature(this);
		return reval;
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

	@Override
	public Set<CommonStructure> getCommonStructureElements() {
		Set<CommonStructure> reval = new HashSet<CommonStructure>();
		reval.addAll(this.predicates);
		return reval;
	}

	@Override
	public Set<CommonTerm> getCommonTermElements() {
		return new HashSet<CommonTerm>();
	}

	@Override
	public void fromSignature(Signature signature) {
		
	}

}
