package angerona.fw.reflection;

import static org.junit.Assert.assertEquals;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import net.sf.tweety.logics.firstorderlogic.syntax.Atom;
import net.sf.tweety.logics.commons.syntax.Predicate;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.asml.Assign;
import angerona.fw.comm.Query;
import angerona.fw.logic.AngeronaAnswer;
import angerona.fw.logic.AnswerValue;
import angerona.fw.serialize.SerializeHelper;

public class AssignTest {
	
	private static Logger LOG = LoggerFactory.getLogger(AssignTest.class);
	
	@Test
	public void testAssignOnEmptyContext() throws ClassNotFoundException {
		Assign assign = new Assign("name", new Value("Angerona"));
		doStringTest(assign, new Context());
	}

	@Test
	public void testDeserialize() {
		String jarPath = "/angerona/fw/reflection/AssignTest.xml";
		InputStream stream = getClass().getResourceAsStream(jarPath);
		if(stream == null)
			LOG.warn("Cannot find: '{}'", jarPath);
		Assign assign = SerializeHelper.loadXml(Assign.class, new InputStreamReader(stream));
		
		doStringTest(assign, new Context());
	}
	
	@Test
	public void testTypeSupport() throws ClassNotFoundException {
		
		// The Helper class triple contains a triple with all information to
		// peform an assign and to test the result.
		class Triple {
			public Class<?> type;
			public String strValue;
			public Object realValue;
			
			public Triple(Class<?> type, String strValue, Object realValue) {
				this.type = type;
				this.strValue = strValue;
				this.realValue = realValue;
			}
		}
		
		// creation of a list of triples for different types, start with build in types:
		List<Triple> lst = new LinkedList<>();
		lst.add(new Triple(Integer.class, "10", new Integer(10)));
		lst.add(new Triple(Float.class, "2.25f", new Float(2.25f)));
		lst.add(new Triple(Double.class, "1.5", new Double(1.5)));
		lst.add(new Triple(Boolean.class, "TRUE", new Boolean(true)));
		
		// test the query as complex type:
		lst.add(new Triple(Query.class, 
				"<query><sender>Boss</sender><receiver>Employee</receiver><question>attend_scm</question></query>", 
				new Query("Boss", "Employee", 
					new FolFormulaVariable(new Atom(new Predicate("attend_scm"))))));
		
		// perform the test:
		Context context = new Context();
		for(Triple t : lst) {
			Assign assign = new Assign("Test", new Value(t.strValue, t.type.getName()));
			assign.execute(context);
			assertEquals(t.realValue, context.get("Test"));
		}
	}
	
	@Test
	public void testContextInternalAssign() throws ClassNotFoundException {
		Context context = new Context();
		
		AngeronaAnswer aa = new AngeronaAnswer(new Atom(new Predicate("attend_scm")), AnswerValue.AV_FALSE);
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
