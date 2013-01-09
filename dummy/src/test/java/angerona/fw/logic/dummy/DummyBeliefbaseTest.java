package angerona.fw.logic.dummy;

import org.junit.Before;

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
	@Before
	public void createBeliefbase() {
		beliefbase = new DummyBeliefbase();
	}
	
}
