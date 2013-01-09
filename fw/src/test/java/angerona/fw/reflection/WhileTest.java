package angerona.fw.reflection;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class WhileTest {
	@Test
	public void testWhile() throws ClassNotFoundException {
		Context context = new Context();
		context.set("run", true);
		
		XMLWhile w = new XMLWhile(new BooleanExpression(new Value("$run", Value.CONTEXT_REFERENCE_TYPE)));
		XMLAssign assign = new XMLAssign("run", new Value("FALSE", Boolean.class.getName()));
		w.addCommando(assign);
		assign = new XMLAssign("sender", "Boss", String.class.getName());
		w.addCommando(assign);
		w.execute(context);
		
		assertEquals(1, w.getIterations());
		assertEquals("Boss", context.get("sender"));
	}
}
