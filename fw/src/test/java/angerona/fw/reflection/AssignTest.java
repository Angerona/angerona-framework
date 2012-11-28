package angerona.fw.reflection;

import java.util.LinkedList;
import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;
import net.sf.tweety.logics.firstorderlogic.syntax.Atom;
import net.sf.tweety.logics.firstorderlogic.syntax.Predicate;
import angerona.fw.comm.Query;
import angerona.fw.logic.AngeronaAnswer;
import angerona.fw.logic.AnswerValue;
import angerona.fw.serialize.SerializeHelper;

public class AssignTest extends TestCase {
	
	public void testAssignOnEmptyContext() throws ClassNotFoundException {
		XMLAssign assign = new XMLAssign("name", "Angerona", null);
		doStringTest(assign, new Context());
	}

	public void testDeserialize() {
		String xml = "<assign name=\"name\" value=\"Angerona\" type=\"string\" />";
		XMLAssign assign = SerializeHelper.loadXml(XMLAssign.class, xml);
		doStringTest(assign, new Context());
	}
	
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
			XMLAssign assign = new XMLAssign("Test", t.strValue, t.type.getName());
			assign.execute(context);
			Assert.assertEquals(t.realValue, context.get("Test"));
		}
	}
	
	public void testContextInternalAssign() throws ClassNotFoundException {
		Context context = new Context();
		
		AngeronaAnswer aa = new AngeronaAnswer(null, new Atom(new Predicate("attend_scm")), AnswerValue.AV_FALSE);
		context.set("answer", aa);
		
		XMLAssign assign = new XMLAssign("reference", "$answer", null);
		assign.execute(context);
		
		Assert.assertEquals(true, context.get("answer") == context.get("reference"));
	}
	
	private void doStringTest(XMLAssign assign, Context c) {
		Assert.assertEquals(true, c.get("name") == null);
		assign.execute(c);
		
		Assert.assertEquals(false, c.get("name") == null);
		Assert.assertEquals(true, c.get("name").equals("Angerona"));
	}
}
