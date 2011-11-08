package net.sf.tweety;

import java.util.Set;

import net.sf.tweety.logics.CommonStructure;
import net.sf.tweety.logics.CommonTerm;

/**
 * A signatures lists the atomic language structures for some language.
 * @author Matthias Thimm, Tim Janus
 */
public abstract class Signature {
	
	/**
	 * builds the signature from the given signature, uses the CommonStrcture
	 * and CommonTerm interfaces.
	 * @param signature
	 */
	public abstract void fromSignature(Signature signature);
	
	/**
	 * builds the signature from the given SymbolSet
	 * @param symbols	the SymbolSet used as basic for the signature.
	 */
	public abstract void fromSymbolSet(SymbolSet symbols);
	
	/** @return a set of all CommonStructure elements used by this signature */
	public abstract Set<CommonStructure> getCommonStructureElements();
	
	/** @return a set of all CommonTerm elements used by this signature */
	public abstract Set<CommonTerm> getCommonTermElements();
	
	/**
	 * Checks whether this signature is a sub-signature of the
	 * given signature, i.e. whether each logical expression expressible
	 * with this signature is also expressible with the given signature.
	 * @param other a signature.
	 * @return "true" iff this signature is a subsignature of the given one.
	 */
	public abstract boolean isSubSignature(Signature other);
	
	/** 
	 * Adds the elements of the given signature to this signature.
	 * @param other a signature.
	 */
	public abstract void addSignature(Signature other);
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	public abstract int hashCode();
	
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public abstract boolean equals(Object obj);
	
	/**
	 * A SymbolSet which can create itself from a Signature.
	 * @author Tim Janus
	 */
	private class RealSymbolSet extends SymbolSetAdapter {

		public RealSymbolSet() {
			super(true);
		}
		
		@Override
		public void fromSignature(Signature sig) {
			
			for(CommonStructure cs : sig.getCommonStructureElements()) {
				if(cs.isPredicate()) {
					symbols.add(cs.getName());
					symbolSorts.put(cs.getName(), cs.getSorts());
				}
			}
			
			for(CommonTerm ct : sig.getCommonTermElements()) {
				if(ct.isConstant()) {
					constants.add(ct.getName());
					constantSorts.put(ct.getName(), ct.getSortName());
				}
			}
		}
	}
	
	/** 
	 * 	This returned set of symbols can be used to translate into other types of signatures.
	 * 	Like external logic implementations which does not use the tweety library.
	 * 	@return a generic set of symbols representing this signature. 
	 */
	public SymbolSet getSymbolSet() {
		RealSymbolSet reval = new RealSymbolSet();
		reval.fromSignature(this);
		return reval;
	}
}
