package angerona.fw.logic.asp;

import static org.junit.Assert.assertEquals;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Program;
import net.sf.tweety.logics.firstorderlogic.syntax.Atom;
import net.sf.tweety.logics.firstorderlogic.syntax.Predicate;

import org.junit.Before;
import org.junit.Test;

import angerona.fw.logic.AngeronaAnswer;
import angerona.fw.logic.AnswerValue;
import angerona.fw.logic.BeliefbaseTest;

/**
 * Tests the behavior of the asp belief base the common tests
 * of BeliefbaseTest will also be run.
 *  
 * @author Tim Janus
 */
public class AspBeliefbaseTest extends BeliefbaseTest<AspBeliefbase> {
	
	@Override
	@Before
	public void createBeliefbase() {
		beliefbase = new AspBeliefbase();
	}
	
	@Test
	public void testDoubleInject() {
		AspBeliefbase bb = new MockBeliefbase();
		
		bb.addKnowledge(new Atom(new Predicate("test")));
		Program p = bb.getProgram();
		assertEquals(1, p.getRules().size());
		
		bb.addKnowledge(new Atom(new Predicate("test")));
		p = bb.getProgram();
		// bug report by Daniel: ticket #68 
		assertEquals(1, p.size());
	}
	
	@Test
	public void testQueryForUnknown() {
		AspBeliefbase bb = new MockBeliefbase();
		AngeronaAnswer aa = bb.getReasoningOperator().query(bb, new Atom(new Predicate("test"))).second;
		// Tests for possible bug in ticket #56 but no bug was found.
		assertEquals(true, aa.getAnswerValue() == AnswerValue.AV_UNKNOWN);
	}
}
