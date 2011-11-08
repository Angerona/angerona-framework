package net.sf.tweety.logicprogramming.asplibrary.syntax;

import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.Signature;
import net.sf.tweety.SymbolSet;
import net.sf.tweety.logics.CommonStructure;
import net.sf.tweety.logics.CommonTerm;

public class ElpSignature extends Signature {

	
	private Set<CommonStructure> predicates;
	
	public ElpSignature() {
		predicates = new HashSet<CommonStructure>();
	}
	
	public void add(Object obj) {
		if(obj == null)	return;
		if(obj instanceof Atom) {
			if(!predicates.contains(obj))
				predicates.add((CommonStructure)obj);
		}
		
	}
	
	@Override
	public void fromSymbolSet(SymbolSet symbols) {
		throw new RuntimeException("Not Implemented yet");
	}

	@Override
	public SymbolSet getSymbolSet() {
		throw new RuntimeException("Not Implemented yet");
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
