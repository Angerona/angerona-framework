package angerona.fw.parser;

import java.io.StringReader;

import angerona.fw.reflection.BooleanExpression;
import angerona.fw.reflection.BooleanExpression.Operator;
import angerona.fw.reflection.Context;
import angerona.fw.reflection.Value;

import junit.framework.TestCase;

/**
 * Unit tests for the DAML Parser.
 * 
 * @author Tim Janus
 */
public class DAMLParserTest extends TestCase {
	public void testParseValue() throws ParseException {
		String toRead = "1.25";
		DAMLParser parser = new DAMLParser(new StringReader(toRead));
		Value val = parser.value();
		
		assertEquals(1.25, val.getValue());
		
		toRead = "100";
		parser.ReInit(new StringReader(toRead));
		val = parser.value();
		assertEquals(100, val.getValue());
		
		toRead = "Boss";
		parser.ReInit(new StringReader(toRead));
		val = parser.value();
		assertEquals("Boss", val.getValue());
		
		Context context = new Context();
		context.set("receiver", "Employee");
		toRead = "$receiver";
		parser.ReInit(new StringReader(toRead));
		val = parser.value();
		assertEquals(Value.CONTEXT_REFERENCE_TYPE, val.getType());
		assertEquals("Employee", val.getValue(context));
	}
	
	public void testParserBooleanExpression() throws ParseException {
		String toRead = "TRUE";
		DAMLParser parser = new DAMLParser(new StringReader(toRead));
		BooleanExpression be = parser.booleanExpression();
		assertEquals(true, be.evaluate());
		
		toRead = "10.5<11";
		parser.ReInit(new StringReader(toRead));
		be = parser.booleanExpression();
		assertEquals(Operator.LESS, be.getOperator());
		assertEquals(10.5, be.getLeft().getValue());
		assertEquals(11, be.getRight().getValue());
	}
}
