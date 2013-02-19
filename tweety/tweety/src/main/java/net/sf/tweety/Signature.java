package net.sf.tweety;

/**
 * A signatures lists the atomic language structures for some language.
 * @author Matthias Thimm
 */
public abstract class Signature {
	
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
}
