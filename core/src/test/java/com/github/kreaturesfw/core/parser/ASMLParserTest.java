package com.github.kreaturesfw.core.parser;

import static org.junit.Assert.assertEquals;

import java.io.StringReader;

import org.junit.Test;

import com.github.kreaturesfw.core.parser.ASMLParser;
import com.github.kreaturesfw.core.parser.ParseException;
import com.github.kreaturesfw.core.reflection.BooleanExpression;
import com.github.kreaturesfw.core.reflection.Context;
import com.github.kreaturesfw.core.reflection.Value;
import com.github.kreaturesfw.core.reflection.BooleanExpression.Operator;

/**
 * Unit tests for the DAML Parser.
 * 
 * @author Tim Janus
 */
public class ASMLParserTest {
	@Test
	public void testParseValue() throws ParseException {
		String toRead = "1.25";
		ASMLParser parser = new ASMLParser(new StringReader(toRead));
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
		val.setContext(context);
		assertEquals(Value.CONTEXT_REFERENCE_TYPE, val.getType());
		assertEquals("Employee", val.getValue());
	}
	
	@Test
	public void testParserBooleanExpression() throws ParseException {
		String toRead = "TRUE";
		ASMLParser parser = new ASMLParser(new StringReader(toRead));
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
