package com.github.angerona.fw.example.logic;

import org.junit.Before;

import com.github.angerona.fw.logic.BeliefbaseTest;

/**
 * Tests the example belief base, actually only the common tests
 * defined in the Angerona Framework are tested here. They prove
 * if the copy behavior is correct.
 * 
 * @author Tim Janus
 *
 */
public class ExampleBeliefbaseTest extends BeliefbaseTest<ExampleBeliefbase> {

	@Override
	@Before
	public void createBeliefbase() {
		beliefbase = new ExampleBeliefbase();
	}
	
}
