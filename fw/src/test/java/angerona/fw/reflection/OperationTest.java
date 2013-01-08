package angerona.fw.reflection;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import junit.framework.Assert;
import junit.framework.TestCase;
import angerona.fw.BaseOperator;
import angerona.fw.OperatorSet;
import angerona.fw.OperatorProvider;
import angerona.fw.error.InvokeException;
import angerona.fw.operators.GenericOperatorParameter;
import angerona.fw.operators.OperatorVisitor;
import angerona.fw.util.Pair;

public class OperationTest extends TestCase {
	private class MockVisitor implements OperatorVisitor {

		private Stack<BaseOperator> stack = new Stack<>();
		
		@Override
		public void pushOperator(BaseOperator op) {
			stack.push(op);
		}

		@Override
		public void popOperator() {
			stack.pop();
		}

		@Override
		public Stack<BaseOperator> getStack() {
			return stack;
		}

	}
	
	private class MockOperation implements BaseOperator {

		@Override
		public void setParameters(Map<String, String> parameters) {
		}

		@Override
		public Map<String, String> getParameters() {
			return null;
		}

		@Override
		public String getParameter(String name, String def) {
			return null;
		}

		@Override
		public Pair<String, Class<?>> getOperationType() {
			return new Pair<String, Class<?>>("MockOperation", MockOperation.class);
		}

		@Override
		public Object process(GenericOperatorParameter gop) {
			return "";
		}
		
	}
	
	public void testMockOperationExceptionNoOperators() {
		Map<String, String> params = new HashMap<String, String>();
		
		Context context = new Context();
		XMLOperation op = new XMLOperation("MockOperation", params, "result");
		
		boolean reval = op.execute(context);
		Assert.assertEquals(false, reval);
		Assert.assertEquals(InvokeException.Type.PARAMETER, op.getLastError().getErrorType());
	}
	
	public void testMockOperationExceptionInternalCauseOperationTypeWrong() {
		Map<String, String> params = new HashMap<String, String>();
		
		MockVisitor mockVisitor = new MockVisitor();
		Context context = new Context();
		XMLOperation op = new XMLOperation("WRONGNAME", params, "result");
		
		context.set("self", mockVisitor);
		OperatorProvider s = new OperatorProvider();
		OperatorSet opSet = new OperatorSet("MockOperation");
		opSet.addOperator(new MockOperation());
		opSet.setPrefered(MockOperation.class.getName());
		s.addOperationSet(opSet);
		context.set("operators", s);
		Assert.assertEquals(false, op.execute(context));
		Assert.assertEquals(InvokeException.Type.INTERNAL, op.getLastError().getErrorType());
	}
	
	public void testMockOperationExceptionNoSelf() {
		Map<String, String> params = new HashMap<String, String>();
		
		Context context = new Context();
		XMLOperation op = new XMLOperation("MockOperation", params, "result");
		
		OperatorProvider s = new OperatorProvider();
		OperatorSet opSet = new OperatorSet("MockOperation");
		opSet.addOperator(new MockOperation());
		opSet.setPrefered(MockOperation.class.getName());
		s.addOperationSet(opSet);
		context.set("operators", s);
		Assert.assertEquals(false, op.execute(context));
		Assert.assertEquals(InvokeException.Type.PARAMETER, op.getLastError().getErrorType());
	}
	
	public void testMockOperationSuccessful() {
		Map<String, String> params = new HashMap<String, String>();
		
		MockVisitor mockVisitor = new MockVisitor();
		Context context = new Context();
		XMLOperation op = new XMLOperation("MockOperation", params, "result");
		
		context.set("self", mockVisitor);
		OperatorProvider s = new OperatorProvider();
		OperatorSet opSet = new OperatorSet("MockOperation");
		opSet.addOperator(new MockOperation());
		opSet.setPrefered(MockOperation.class.getName());
		s.addOperationSet(opSet);
		context.set("operators", s);
		Assert.assertEquals(true, op.execute(context));
		Assert.assertEquals("", context.get("result"));
	}
}