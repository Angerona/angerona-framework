package com.github.kreaturesfw.ocf.logic;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import com.github.kreaturesfw.core.parser.ParseException;

import net.sf.tweety.logics.cl.semantics.RankingFunction;
import net.sf.tweety.logics.fol.parser.FolParserB;
import net.sf.tweety.logics.fol.syntax.FolFormula;
import net.sf.tweety.logics.fol.syntax.FolSignature;


public class OCFTester {
	
//	public static String rawbbase = "(d22|s22) \n (d11|s11) \n (r|d22 && d11) \n (-d22 || -d11 | s22 && s11 && i11_22) \n (d21|s21) \n (r|d21 && d11)";
	public static String rawbbase = "(c|s1) \n (!s3|s2) \n (!s2|s3) \n (!s1 | s4)";
	
	public static void main(String[] args) throws ParseException, IOException, net.sf.tweety.logics.fol.parser.ParseException {
		ConditionalBeliefbase bbase = new ConditionalBeliefbase();
		bbase.parse(new BufferedReader(new StringReader(rawbbase)));
		ConditionalReasoner bbasereasoner = new ConditionalReasoner();
				
		
		System.out.println("Initial beliefbase:");
		System.out.println(bbase.toString());
		
		System.out.println("c-representation:");
		RankingFunction ocf = bbasereasoner.calculateCRepresentation(bbase.getConditionalBeliefs());
		System.out.println(ocf);
		
		String fol = "a || b";
		
		
		FolParserB parser = new FolParserB(new StringReader(fol));
		FolFormula formula = parser.formula(new FolSignature());
		System.out.println(fol + " => " + formula.toString());
		
	}
	
	

//	Proposition s22 = new Proposition("s22");
//	Proposition d22 = new Proposition("d22");
//	Proposition s11 = new Proposition("s11");
//	Proposition d11 = new Proposition("d11");
//	Proposition s21 = new Proposition("s21");
//	Proposition d21 = new Proposition("d21");
//	Proposition i11_22 = new Proposition("i11_22");
//	Proposition r = new Proposition("r");
//	
//	Conditional c1 = new Conditional(s22, d22);
//	Conditional c2 = new Conditional(s11,d11);
//	
//	Conjunction c3p = new Conjunction();
//	c3p.add(d22);
//	c3p.add(d11);
//	Conditional c3 = new Conditional(c3p,r);
}
