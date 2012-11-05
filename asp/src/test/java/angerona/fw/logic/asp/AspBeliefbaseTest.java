package angerona.fw.logic.asp;

import net.sf.tweety.logicprogramming.asplibrary.syntax.Program;
import net.sf.tweety.logics.firstorderlogic.syntax.Atom;
import net.sf.tweety.logics.firstorderlogic.syntax.Predicate;
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
	
	public void testDoubleInject() {
		AspBeliefbase bb = new MockBeliefbase();
		
		bb.addKnowledge(new Atom(new Predicate("test")));
		Program p = bb.getProgram();
		assertEquals(1, p.size());
		
		bb.addKnowledge(new Atom(new Predicate("test")));
		p = bb.getProgram();
		// TODO bug report by Daniel: ticket #68 
		//assertEquals(1, p.size());
	}
}
