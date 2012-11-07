package net.sf.logicprogramming.asplibrary.syntax;

import java.io.StringReader;

import junit.framework.TestCase;
import net.sf.tweety.logicprogramming.asplibrary.parser.ELPParser;
import net.sf.tweety.logicprogramming.asplibrary.parser.ParseException;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Atom;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Constant;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Literal;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Relation;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Rule;
import net.sf.tweety.logicprogramming.asplibrary.syntax.Variable;

public class ParserTest extends TestCase {
	
	public void testRuleParsing() {
		String str = "test :- predWithConstant(bobby).";
		ELPParser parser = new ELPParser(new StringReader(str));
		try {
			Rule r = parser.rule();
			assertEquals(new Atom("test"), r.getHead().get(0));
			assertEquals(new Atom("predWithConstant", new Constant("bobby")), r.getBody().get(0));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void testRelationParsing() {
		String str = "X < Y";
		ELPParser parser = new ELPParser(new StringReader(str));
		try {
			Literal a = parser.LiteralExpr();
			assertEquals(new Relation("<", new Variable("X"), new Variable("Y")), a);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void testDoubleArgument() {
		String str = "info(excused(a_SELF))";
		ELPParser parser = new ELPParser(new StringReader(str));
		try {
			Literal a = parser.LiteralExpr();
			assertEquals(new Atom("info", new Constant("excused(a_SELF)")), a);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
