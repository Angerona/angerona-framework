package angerona.fw.logic;

import angerona.fw.BaseBeliefbase;
import junit.framework.TestCase;

/**
 * A generic test class testing the generic interface of different
 * types of beliefbases. There are some things a ASP and a OCF beliefbase
 * have in common these class adds test methods for these common conditions.
 *
 * @author Tim Janus
 *
 * @param <T> The concrete of the beliefbase like ASP beliefbase
 */
public abstract class BeliefbaseTest<T extends BaseBeliefbase> extends TestCase {
	
	/** 
	 * Creates an instance of a subclass of BaseBeliefbase the type of this
	 * instance is T. The base class needs this method to perform general
	 * tests on every belief base type.
	 * @return	The newly created instance of a belief base of Type T.
	 */
	protected abstract T createBeliefbase();

	/**
	 * Tests the copy behavior. If a belief base is copied the data is
	 * copied but the ids are not allowed to change.
	 */
	public void testCopy() {
		T bb = createBeliefbase();
		@SuppressWarnings("unchecked")
		T copy = (T)bb.clone();
		assertEquals(bb.getGUID(), copy.getGUID());
		assertEquals(bb.getParent(), copy.getParent());
		assertEquals(bb.getChilds(), copy.getChilds());
	}
	
}
