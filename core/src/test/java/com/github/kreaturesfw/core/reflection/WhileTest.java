package com.github.kreaturesfw.core.reflection;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.kreaturesfw.core.asml.Assign;
import com.github.kreaturesfw.core.asml.While;
import com.github.kreaturesfw.core.reflection.BooleanExpression;
import com.github.kreaturesfw.core.reflection.Context;
import com.github.kreaturesfw.core.reflection.Value;
import com.github.kreaturesfw.core.serialize.SerializeHelper;

public class WhileTest {
	
	private static Logger LOG = LoggerFactory.getLogger(WhileTest.class);
	
	private While loop;
	
	@Before
	public void setUp() {
		// load file containing the following code:
		// if: left < right: result=1
		// else if: left == right: result=2
		// else: result=3
		
		String jarPath = "/com/github/angerona/fw/reflection/WhileTest.xml";
		InputStream stream = getClass().getResourceAsStream(jarPath);
		if(stream == null) {
			LOG.warn("Cannot find: '{}'", jarPath);
			return ;
		}
		loop = SerializeHelper.get().loadXmlTry(While.class, new InputStreamReader(stream));
	}
	
	@Test
	public void testManualWhile() throws ClassNotFoundException {
		Context context = new Context();
		context.set("run", true);
		
		While w = new While(new BooleanExpression(new Value("$run", Value.CONTEXT_REFERENCE_TYPE)));
		Assign assign = new Assign("run", new Value("FALSE", Boolean.class.getName()));
		w.addCommando(assign);
		assign = new Assign("sender", new Value("Boss"));
		w.addCommando(assign);
		w.execute(context);
		
		assertEquals(1, w.getIterations());
		assertEquals("Boss", context.get("sender"));
	}
	
	@Test
	public void testWhileXML() {
		Context context = new Context();
		
		loop.execute(context);
		assertEquals("Blub", context.get("counter"));
		assertEquals(1, loop.getIterations());
	}
}
