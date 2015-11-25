package com.github.kreaturesfw.core.logic;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.github.kreaturesfw.core.BaseBeliefbase;

/**
 * A generic test class testing the generic interface of different
 * types of beliefbases. There are some things a ASP and a OCF beliefbase
 * have in common these class adds test methods for these common conditions.
 *
 * @author Tim Janus
 *
 * @param <T> The concrete of the beliefbase like ASP beliefbase
 */
public abstract class BeliefbaseTest<T extends BaseBeliefbase> {
	
	protected T beliefbase;
	
	/** 
	 * Creates an instance of a subclass of BaseBeliefbase the type of this
	 * instance is T. The base class needs this method to perform general
	 * tests on every belief base type.
	 * @return	The newly created instance of a belief base of Type T.
	 */
	@Before
	public abstract void createBeliefbase();

	/**
	 * Tests the copy behavior. If a belief base is copied the data is
	 * copied but the ids are not allowed to change.
	 */
	@Test
	public void testCopy() {
		@SuppressWarnings("unchecked")
		T copy = (T)beliefbase.clone();
		assertEquals(beliefbase.getGUID(), copy.getGUID());
		assertEquals(beliefbase.getParent(), copy.getParent());
		assertEquals(beliefbase.getChilds(), copy.getChilds());
	}
	
}
