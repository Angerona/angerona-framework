package com.github.angerona.fw.logic.asp;

import static org.junit.Assert.assertEquals;

import java.util.Set;

import net.sf.tweety.logics.commons.syntax.Predicate;
import net.sf.tweety.logics.fol.syntax.FOLAtom;
import net.sf.tweety.logics.fol.syntax.FolFormula;
import net.sf.tweety.lp.asp.syntax.Program;

import org.junit.Before;
import org.junit.Test;

import com.github.angerona.fw.logic.AngeronaAnswer;
import com.github.angerona.fw.logic.AnswerValue;
import com.github.angerona.fw.logic.BeliefbaseTest;
import com.github.angerona.fw.operators.parameter.ReasonerParameter;
import com.github.angerona.fw.util.Pair;

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
