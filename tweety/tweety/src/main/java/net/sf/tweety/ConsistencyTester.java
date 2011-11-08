package net.sf.tweety;

/**
 * Classes implementing this interface are capable of testing
 * whether a given belief base is consistent.
 * 
 * @author Matthias Thimm
 */
public interface ConsistencyTester {
	
	/**
	 * Checks whether the given belief base is consistent.
	 * @param beliefBase a belief base.
	 * @return "true" iff the given belief base is consistent.
	 */
	public boolean isConsistent(BeliefBase beliefBase);
}
