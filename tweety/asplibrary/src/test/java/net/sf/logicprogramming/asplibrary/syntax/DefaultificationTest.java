package net.sf.logicprogramming.asplibrary.syntax;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Atom;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Neg;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Not;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Program;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Rule;

public class DefaultificationTest extends TestCase {
	/**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public DefaultificationTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( DefaultificationTest.class );
    }
    
    public void testDefaultifcation() {
    	Program p = new Program();
    	Rule onlyRule = new Rule();
    	onlyRule.addHead(new Atom("x"));
    	onlyRule.addBody(new Atom("y"));
    	p.add(onlyRule);
    	
    	Program dp = Program.defaultification(p);
    	assertTrue(dp.size() == p.size());
    	Rule dr = dp.get(0);
    	return; 
    	// TODO: attach debugger to test using maven test goal
    	/*
    	assertTrue(dr.getHead().equals(onlyRule.getHead()));
    	assertTrue(dr.getBody().contains(onlyRule.getBody().get(0)));
    	Not defNot = new Not(new Neg(dr.getHead().get(0).getAtom()));
    	assertTrue(dr.getBody().contains(defNot));
    	*/
    }
}
