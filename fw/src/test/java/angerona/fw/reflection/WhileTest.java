package angerona.fw.reflection;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import angerona.fw.asml.Assign;
import angerona.fw.asml.While;

public class WhileTest {
	@Test
	public void testWhile() throws ClassNotFoundException {
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
}
