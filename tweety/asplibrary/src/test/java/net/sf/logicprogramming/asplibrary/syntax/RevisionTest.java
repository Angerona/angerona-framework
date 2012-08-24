package net.sf.logicprogramming.asplibrary.syntax;

import java.io.StringReader;

import junit.framework.TestCase;
import net.sf.tweety.logicprogramming.asplibrary.revision.PreferenceHandling;
import net.sf.tweety.logicprogramming.asplibrary.revision.RevisionApproach;
import net.sf.tweety.logicprogramming.asplibrary.solver.DLVComplex;
import net.sf.tweety.logicprogramming.asplibrary.solver.Solver;
import net.sf.tweety.logicprogramming.asplibrary.solver.SolverException;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Program;
import net.sf.tweety.logicprogramming.asplibrary.util.AnswerSetList;

public class RevisionTest extends TestCase {
	class MinQualityTest {
		private RevisionApproach approach;
		
		public MinQualityTest(RevisionApproach ra) {
			approach = ra;
		}
		
		/** P is in K*P */
		public void testAGM2() throws SolverException {
			
			// We update the Program with contradictionary knowledge... we
			// assume the newer knowledge will be used and the old knowledge
			// deleted:
			Program p1 = Program.loadFrom(new StringReader("a."));
			Program p2 = Program.loadFrom(new StringReader("-a."));
			Program pr = approach.revision(p1, p2, solver);
			
			// has new knowledge?
			TestCase.assertEquals(true, pr.containsAll(p2));
			
			// no contradiction?
			TestCase.assertEquals(false, pr.contains(p1.get(0)));
		}
		
		/** K*P is inconsistent only if: K or P is inconsistent beforehand (is this testable?) */
		public void testAGM4() throws SolverException {
			
		}
		
		/** if p and q are logically equivalent then K*P = K*Q 
		 * @throws SolverException */
		public void testAGM6() throws SolverException {
			Program p = Program.loadFrom(new StringReader("a. b."));
			Program q = Program.loadFrom(new StringReader("b :- a."));
			Program src = Program.loadFrom(new StringReader("c."));
			
			Program sp = approach.revision(src, p, solver);
			Program sq = approach.revision(src, q, solver);
			AnswerSetList aslp = solver.computeModels(sp, 5);
			AnswerSetList aslq = solver.computeModels(sq, 5);
			
			// todo check for equality of the answersets to determine
			// if the programs are equal.
		}
	}
	
	private Solver solver = new DLVComplex("D:\\Tim\\HiWi\\angerona\\software\\test\\src\\main\\tools\\win\\solver\\asp\\dlv\\dlv-complex.exe");
	
	/**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public RevisionTest( String testName )
    {
        super( testName );
    }
	
	public void testMinQuality() throws SolverException {
		// First of all Test Preference Handling:
		/*
		MinQualityTest mqt_ph = new MinQualityTest(new PreferenceHandling());
		mqt_ph.testAGM2();
		*/
	}
	
	public void testDanielsBugReport() throws SolverException {
		/*
		PreferenceHandling pf = new PreferenceHandling();
		// is this really a bug or is this the wished behavior?
		// Differentiate between rules which can change facts and those which cannot.
		Program p1 = Program.loadFrom(new StringReader("john :- deep_voice."));
		Program p2 = Program.loadFrom(new StringReader("-john."));
		Program pr = pf.revision(p1, p2, solver);
		System.out.println("Program:\n"+pr.toString());
		
		Program p3 = Program.loadFrom(new StringReader("deep_voice."));
		pr = pf.revision(pr, p3, solver);
		System.out.println("Program:\n"+pr);
		*/
	}
	
	public void testPatricksBugReport() throws SolverException {
		/*
		PreferenceHandling pf = new PreferenceHandling();
		
		Program p1 = Program.loadFrom(new StringReader("a :- not b. -a :- c."));
		Program p2 = Program.loadFrom(new StringReader("c."));
		Program pr = pf.revision(p1, p2, solver);
		System.out.println(pr);
		*/
	}
}
