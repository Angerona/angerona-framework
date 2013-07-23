package angerona.fw.logic.asp;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import net.sf.tweety.logicprogramming.asplibrary.syntax.Program;
import net.sf.tweety.logics.commons.syntax.Predicate;
import net.sf.tweety.logics.firstorderlogic.syntax.FOLAtom;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;

import org.junit.Before;
import org.junit.Test;

import angerona.fw.logic.AngeronaAnswer;
import angerona.fw.logic.AnswerValue;
import angerona.fw.logic.BeliefbaseTest;
import angerona.fw.operators.parameter.ReasonerParameter;
import angerona.fw.util.Pair;

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
	public void testDoubleInject() throws InstantiationException {
		AspBeliefbase bb = new MockBeliefbase();
		
		bb.addKnowledge(new FOLAtom(new Predicate("test")));
		Program p = bb.getProgram();
		assertEquals(1, p.size());
		
		bb.addKnowledge(new FOLAtom(new Predicate("test")));
		p = bb.getProgram();
		// bug report by Daniel: ticket #68 
		assertEquals(1, p.size());
	}
	
	@Test
	public void testQueryForUnknown() throws InstantiationException {
		AspBeliefbase bb = new MockBeliefbase();
		@SuppressWarnings("unchecked")
		Pair<Set<FolFormula>, AngeronaAnswer> reval = 
				(Pair<Set<FolFormula>, AngeronaAnswer>) bb.getReasoningOperator().process(
				new ReasonerParameter(bb, new FOLAtom(new Predicate("test"))));
		// Tests for possible bug in ticket #56 but no bug was found.
		assertEquals(true, reval.second.getAnswerValue() == AnswerValue.AV_UNKNOWN);
	}
}
