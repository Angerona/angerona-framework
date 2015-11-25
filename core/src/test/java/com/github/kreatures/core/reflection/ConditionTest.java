package com.github.kreatures.core.reflection;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.github.kreatures.core.reflection.BooleanExpression.Operator;


public class ConditionTest {
	@Test
	public void testUnaryCondition() throws ClassNotFoundException {
		BooleanExpression be = new BooleanExpression(new Value("TRUE", Boolean.class.getName()));
		assertEquals(true, be.evaluate());
		
		be = new BooleanExpression(new Value("FALSE", Boolean.class.getName()));
		assertEquals(false, be.evaluate());
		
		Context emptyContext = new Context();
		be = new BooleanExpression(new Value("$reference", "REF"));
		
		be.setContext(emptyContext);
		assertEquals(false, be.evaluate());
		
		be = new BooleanExpression(new Value("Mein Name", String.class.getName()));
		assertEquals(false, be.evaluate());
	}
	
	@Test
	public void testBinaryCondition() throws ClassNotFoundException {
		Value boss = new Value("Boss");
		Value employee = new Value("Employee");
		BooleanExpression be = new BooleanExpression(boss, Operator.EQUAL, employee);
		assertEquals(false, be.evaluate());
		
		be = new BooleanExpression(boss, Operator.NOTEQUAL, employee);
		assertEquals(true, be.evaluate());
		
		Value dTen = new Value("10", Double.class.getName());
		Value iFive = new Value("5", Integer.class.getName());
		Value dFive = new Value("5.0", Double.class.getName());
		
		be = new BooleanExpression(dTen, Operator.LESS, iFive);
		assertEquals(false, be.evaluate());
		
		be = new BooleanExpression(iFive, Operator.LESSEQ, dFive);
		assertEquals(true, be.evaluate());
		
		be = new BooleanExpression(iFive, Operator.GREATER, dFive);
		assertEquals(false, be.evaluate());
		
	}
}
