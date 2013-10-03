package com.github.angerona.fw.reflection;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;
import java.io.InputStreamReader;

import net.sf.tweety.logics.commons.syntax.Predicate;
import net.sf.tweety.logics.firstorderlogic.syntax.FOLAtom;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.angerona.fw.asml.Assign;
import com.github.angerona.fw.comm.Query;
import com.github.angerona.fw.def.FrameworkPlugin;
import com.github.angerona.fw.logic.AngeronaAnswer;
import com.github.angerona.fw.logic.AnswerValue;
import com.github.angerona.fw.serialize.SerializeHelper;

public class AssignTest {
	
	private static Logger LOG = LoggerFactory.getLogger(AssignTest.class);
	
	@Before
	public void initSerializer() {
		FrameworkPlugin fp = new FrameworkPlugin();
		fp.onLoading();
	}
	
	@Test
	public void testAssignOnEmptyContext() throws ClassNotFoundException {
		Assign assign = new Assign("name", new Value("Angerona"));
		doStringTest(assign, new Context());
	}

	@Test
	public void testDeserialize() {
		String jarPath = "/com/github/angerona/fw/reflection/AssignTest.xml";
		InputStream stream = getClass().getResourceAsStream(jarPath);
		if(stream == null) {
			LOG.warn("Cannot find: '{}'", jarPath);
			return;
		}
		Assign assign = SerializeHelper.get().loadXmlTry(Assign.class, new InputStreamReader(stream));
		
		doStringTest(assign, new Context());
	}
	
	// The Helper class triple contains a triple with all information to
	// peform an assign and to test the result.
	static class Triple {
		public Class<?> type;
		public String strValue;
		public Object realValue;
		
		public Triple(Class<?> type, String strValue, Object realValue) {
			this.type = type;
			this.strValue = strValue;
			this.realValue = realValue;
		}
	}
	
	private void testTypeSupport(Triple t) throws ClassNotFoundException {
		Context context = new Context();
		Assign assign = new Assign("Test", new Value(t.strValue, t.type.getName()));
		assign.execute(context);
		assertEquals(t.realValue, context.get("Test"));
	}
	
	@Test
	public void testTypeInteger() throws ClassNotFoundException {
		testTypeSupport(new Triple(Integer.class, "10", Integer.valueOf(10)));
	}
	
	@Test
	public void testTypeFloat() throws ClassNotFoundException {
		testTypeSupport(new Triple(Float.class, "2.25f", new Float(2.25f)));
	}
	
	@Test
	public void testTypeDouble() throws ClassNotFoundException {
		testTypeSupport(new Triple(Double.class, "1.5", new Double(1.5)));
	}
	
	public void testTypeBoolean() throws ClassNotFoundException {
		testTypeSupport(new Triple(Boolean.class, "TRUE", Boolean.valueOf(true)));
	}
	
	@Test
	public void testTypeComplexQuery() throws ClassNotFoundException {
		testTypeSupport(new Triple(Query.class, 
				"<query><sender>Boss</sender><receiver>Employee</receiver><question>attend_scm</question></query>", 
				new Query("Boss", "Employee", 
					new FolFormulaVariable(new FOLAtom(new Predicate("attend_scm"))))));
	}
	
	@Test
	public void testContextInternalAssign() throws ClassNotFoundException {
		Context context = new Context();
		
		AngeronaAnswer aa = new AngeronaAnswer(new FOLAtom(new Predicate("attend_scm")), AnswerValue.AV_FALSE);
		context.set("answer", aa);
		
		Assign assign = new Assign("reference", new Value("$answer", null));
		assign.execute(context);
		
		assertEquals(true, context.get("answer") == context.get("reference"));
	}
	
	private void doStringTest(Assign assign, Context c) {
		assertEquals(true, c.get("name") == null);
		assign.execute(c);
		
		assertEquals(false, c.get("name") == null);
		assertEquals(true, c.get("name").equals("Angerona"));
	}
}
