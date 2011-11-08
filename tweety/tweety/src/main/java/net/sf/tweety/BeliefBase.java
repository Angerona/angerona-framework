package net.sf.tweety;

/**
 * This class captures an abstract knowledge base, i.e. some set of
 * formulas in a given knowledge representation language, that can be asked
 * queries.
 * @author Matthias Thimm
 */
public abstract class BeliefBase {
	
	/**
	 * Returns the signature of the language of this knowledge base.
	 * @return the signature of the language of this knowledge base.
	 */
	public abstract Signature getSignature();
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public abstract String toString();
}
