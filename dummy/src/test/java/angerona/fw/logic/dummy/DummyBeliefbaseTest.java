package angerona.fw.logic.dummy;

import angerona.fw.logic.BeliefbaseTest;

/**
 * Tests the Dummy Beliefbase, actually only the common tests
 * defined in the Angerona Framework are tested here.
 * 
 * @author Tim Janus
 *
 */
public class DummyBeliefbaseTest extends BeliefbaseTest<DummyBeliefbase> {

	@Override
	protected DummyBeliefbase createBeliefbase() {
		return new DummyBeliefbase();
	}
	
}
