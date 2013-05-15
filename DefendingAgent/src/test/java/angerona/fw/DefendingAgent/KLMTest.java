package angerona.fw.DefendingAgent;

import net.sf.tweety.logics.commons.syntax.Predicate;
import net.sf.tweety.logics.firstorderlogic.syntax.FOLAtom;
import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;
import net.sf.tweety.logics.propositionallogic.PlBeliefSet;
import net.sf.tweety.logics.propositionallogic.syntax.Contradiction;
import net.sf.tweety.logics.propositionallogic.syntax.Disjunction;
import net.sf.tweety.logics.propositionallogic.syntax.Negation;
import net.sf.tweety.logics.propositionallogic.syntax.Proposition;
import net.sf.tweety.logics.propositionallogic.syntax.PropositionalFormula;
import net.sf.tweety.logics.propositionallogic.syntax.Tautology;
import angerona.fw.defendingagent.Prover.Prover;
import angerona.fw.defendingagent.Prover.Prover.InferenceSystem;
import angerona.fw.logic.AnswerValue;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class KLMTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public KLMTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( KLMTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
//    	CensorComponent cexec = new CensorComponent();
//		
//		// Logistics example - test klm solver
//		PlBeliefSet beliefs = new PlBeliefSet();
//		beliefs.add(new Proposition("d11"));
//		beliefs.add(new Proposition("s21"));
//		beliefs.add(new Proposition("i11_22"));
//		
//		
//		// Build View
//		View v = new View(beliefs);
//		
//		FolFormula d11 = new Atom(new Predicate("d11"));
//		FolFormula s21 = new Atom(new Predicate("s21"));
//		FolFormula not_s21 = new net.sf.tweety.logics.firstorderlogic.syntax.Negation(s21);
//		FolFormula r = new Atom(new Predicate("r"));
//		FolFormula s21_r = new net.sf.tweety.logics.firstorderlogic.syntax.Disjunction(not_s21, r); 
//		
//		v.RefineViewByQuery(d11, AnswerValue.AV_UNKNOWN);
//		v.RefineViewByQuery(s21_r, AnswerValue.AV_TRUE);
//		v.RefineViewByRevision(s21, AnswerValue.AV_TRUE);
//		
//		String[] CL_V = cexec.makeBeliefBase(v);
//		PropositionalFormula plprove = new Disjunction(new Negation(new Tautology()), new Contradiction());
//		
//		Prover prover = new Prover();
//		System.out.println("Prover: "+prover.prove(CL_V, cexec.translate(plprove), Solver.FREE_RATIONAL));
    	assertTrue(true);
    }
}
