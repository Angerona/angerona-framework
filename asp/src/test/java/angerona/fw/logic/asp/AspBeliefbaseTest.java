package angerona.fw.logic.asp;

import angerona.fw.logic.BeliefbaseTest;

/**
 * Tests the behavior of the asp belief base the common tests
 * of BeliefbaseTest will also be run.
 *  
 * @author Tim Janus
 */
public class AspBeliefbaseTest extends BeliefbaseTest<AspBeliefbase> {
	@Override
	protected AspBeliefbase createBeliefbase() {
		return new AspBeliefbase();
	}
}
