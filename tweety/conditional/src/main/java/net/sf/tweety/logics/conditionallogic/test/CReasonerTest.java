package net.sf.tweety.logics.conditionallogic.test;

import net.sf.tweety.logics.conditionallogic.*;
import net.sf.tweety.logics.conditionallogic.syntax.*;
import net.sf.tweety.logics.propositionallogic.syntax.*;

public class CReasonerTest {
	public static void main(String[] args){
		Proposition f = new Proposition("f");
		Proposition b = new Proposition("b");
		Proposition p = new Proposition("p");
		
		Conditional c1 = new Conditional(b,f);
		Conditional c2 = new Conditional(p,b);
		Conditional c3 = new Conditional(p,new Negation(f));
		
		ClBeliefSet bs = new ClBeliefSet();
		bs.add(c1);
		bs.add(c2);
		bs.add(c3);
		
		System.out.println(bs);
		
		BruteForceCReasoner reasoner = new BruteForceCReasoner(bs);
		
		System.out.println(reasoner.getCRepresentation());
		
	}
}
