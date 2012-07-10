package net.sf.tweety.logicprogramming.asplibrary.parser;

import java.io.StringReader;
import java.util.List;

import net.sf.tweety.logicprogramming.asplibrary.syntax.Atom;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Literal;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Program;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Rule;

public class ParserFuncTest {
	public static void main(String [] args) throws ParseException {
		String rule = "-who_argued(john) :- not someone_argued.";
		ELPParser parser = new ELPParser(new StringReader(rule));
		
		Program p = new Program();
		List<Rule> rules = parser.program();
		p.addAll(rules);
		
		System.out.println("Analyse der Parser-Ausgabe: " + rule);
		for(Rule r : p) {
			Atom head = r.getHead().get(0).getAtom();
			Output(head);
			System.out.println();
			System.out.println("Body:");
			for(Literal l : r.getBody()) {
				Output(l.getAtom());
			}
		}
	}
	
	public static void Output(Atom atom) {
		System.out.println(atom.getName() + "/" + atom.getArity());
		System.out.println("Is StrictNegated: " + atom.isTrueNegated());
		System.out.println("Is DefaultNegated: " + atom.isDefaultNegated());
		
		for(int i=0; i<atom.getArity(); ++i) {
			System.out.println("IsConstant: "+atom.getTerm(i).isConstant());
			System.out.println("IsList: "+atom.getTerm(i).isList());
			System.out.println("IsSet: "+atom.getTerm(i).isSet());
			System.out.println("IsVariable: "+atom.getTerm(i).isVariable());
			System.out.println("IsNumber: "+atom.getTerm(i).isNumber());
		}
	}
}
