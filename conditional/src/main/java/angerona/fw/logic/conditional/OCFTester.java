package angerona.fw.logic.conditional;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

import net.sf.tweety.logics.conditionallogic.ClBeliefSet;
import net.sf.tweety.logics.conditionallogic.semantics.RankingFunction;
import net.sf.tweety.logics.conditionallogic.syntax.Conditional;
import net.sf.tweety.logics.propositionallogic.syntax.Conjunction;
import net.sf.tweety.logics.propositionallogic.syntax.Proposition;
import angerona.fw.parser.ParseException;


public class OCFTester {
	
	public static String rawbbase = "(d22|s22) \n (d11|s11) \n (r|d22 && d11) \n (-d22 || -d11 | s22 && s11 && i11_22) \n (d21|s21) \n (r|d21 && d11)";
	public static String rawrev1 = "s22";
	public static String rawrev2 = "s11";
	public static String rawrev3 = "i11_22";
	public static String rawrev4 = "s21";
	
	public static void main(String[] args) throws ParseException, IOException {
		ConditionalBeliefbase bbase = new ConditionalBeliefbase();
		bbase.parse(new BufferedReader(new StringReader(rawbbase)));
		ConditionalReasoner bbasereasoner = new ConditionalReasoner(bbase);
		
		ConditionalBeliefbase rev1 = new ConditionalBeliefbase();
		rev1.parse(new BufferedReader(new StringReader(rawbbase + "\n" + rawrev1)));
		ConditionalReasoner rev1reasoner = new ConditionalReasoner(rev1);
		
		ConditionalBeliefbase rev2 = new ConditionalBeliefbase();
		rev2.parse(new BufferedReader(new StringReader(rawbbase + "\n" + rawrev1 + "\n" + rawrev2)));
		ConditionalReasoner rev2reasoner = new ConditionalReasoner(rev2);
		
		ConditionalBeliefbase rev3 = new ConditionalBeliefbase();
		rev3.parse(new BufferedReader(new StringReader(rawbbase + "\n" + rawrev1 + "\n" + rawrev2 + "\n" + rawrev3)));
		ConditionalReasoner rev3reasoner = new ConditionalReasoner(rev3);
		
		ConditionalBeliefbase rev4 = new ConditionalBeliefbase();
		rev4.parse(new BufferedReader(new StringReader(rawbbase + "\n" + rawrev1 + "\n" + rawrev2 + "\n" + rawrev3 + "\n" + rawrev4)));
		ConditionalReasoner rev4reasoner = new ConditionalReasoner(rev4);
		
		
		System.out.println("Initial beliefbase:");
		System.out.println(bbase.toString());
		
		System.out.println("c-representation:");
		bbasereasoner.calculateCRepresentation();
		RankingFunction ocf = bbasereasoner.ocf;
		System.out.println(ocf);
		
		rev1reasoner.ocf = ocf;
		rev2reasoner.ocf = ocf;
		rev3reasoner.ocf = ocf;
		rev4reasoner.ocf = ocf;
		
		
		System.out.println("\n propositions:");
		System.out.println(rev1.getPropositions());
		System.out.println("propositional beliefs:");
		System.out.println(rev1reasoner.inferInt());
		
		System.out.println("\n results of revision by " + rawrev2);
		System.out.println(rev2reasoner.inferInt());
		
		System.out.println("\n results of revision by " + rawrev3);
		System.out.println(rev3reasoner.inferInt());
		
		System.out.println("\n results of revision by " + rawrev4);
		System.out.println(rev4reasoner.inferInt());
		
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
