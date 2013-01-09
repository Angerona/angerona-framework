package angerona.fw.reflection;

import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import angerona.fw.serialize.SerializeHelper;

/**
 * Tests the Conditional construct of the ASML language.
 * @author Tim Janus
 */
public class ConditionalTest {
	private XMLConditional conditional;
	
	@Before
	public void setUp() {
		// load file containing the following code:
		// if: left < right: result=1
		// else if: left == right: result=2
		// else: result=3
		
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		InputStream stream = loader.getResourceAsStream("angerona/fw/reflection/ConditionalTest.xml");
		conditional = SerializeHelper.loadXml(XMLConditional.class, new InputStreamReader(stream));
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
