package com.github.kreaturesfw.core.reflection;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.kreaturesfw.core.asml.Conditional;
import com.github.kreaturesfw.core.reflection.Context;
import com.github.kreaturesfw.core.serialize.SerializeHelper;

/**
 * Tests the Conditional construct of the ASML language.
 * @author Tim Janus
 */
public class ConditionalTest {
	private Conditional conditional;
	
	private static Logger LOG = LoggerFactory.getLogger(ConditionalTest.class);
	
	@Before
	public void setUp() {
		// load file containing the following code:
		// if: left < right: result=1
		// else if: left == right: result=2
		// else: result=3
		
		String jarPath = "/com/github/angerona/fw/reflection/ConditionalTest.xml";
		InputStream stream = getClass().getResourceAsStream(jarPath);
		if(stream == null) {
			LOG.warn("Cannot find: '{}'", jarPath);
			return;
		}
		conditional = SerializeHelper.get().loadXmlTry(Conditional.class, new InputStreamReader(stream));
	}
	
	@After 
	public void cleanUp() {
		conditional = null;
	}
	
	@Test
	public void testIfTrue() {
		Context c = new Context();
		c.set("left", 1);
		c.set("right", 2);
		
		conditional.execute(c);
		assertEquals(1, c.get("result"));
	}
	
	@Test
	public void testIfElseTrue() {
		Context c = new Context();
		c.set("left", 2);
		c.set("right", 2);
		
		conditional.execute(c);
		assertEquals(2, c.get("result"));
	}
	
	@Test
	public void testElseTrue() {
		Context c = new Context();
		c.set("left", 3);
		c.set("right", 2);
		
		conditional.execute(c);
		assertEquals(3, c.get("result"));
	}
}
