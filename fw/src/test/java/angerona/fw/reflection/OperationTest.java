package angerona.fw.reflection;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.junit.Test;

import angerona.fw.BaseOperator;
import angerona.fw.OperatorProvider;
import angerona.fw.OperatorSet;
import angerona.fw.asml.InvokeOperation;
import angerona.fw.error.InvokeException;
import angerona.fw.operators.GenericOperatorParameter;
import angerona.fw.operators.OperatorVisitor;
import angerona.fw.util.Pair;

public class OperationTest {
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
	
	@Test
	public void testMockOperationExceptionNoOperators() {
		Map<String, Value> params = new HashMap<String, Value>();
		
		Context context = new Context();
		InvokeOperation op = new InvokeOperation("MockOperation", params, "result");
		
		boolean reval = op.execute(context);
		assertEquals(false, reval);
		assertEquals(InvokeException.Type.PARAMETER, op.getLastError().getErrorType());
	}
	
	@Test
	public void testMockOperationExceptionInternalCauseOperationTypeWrong() {
		Map<String, Value> params = new HashMap<String, Value>();
		
		MockVisitor mockVisitor = new MockVisitor();
		Context context = new Context();
		InvokeOperation op = new InvokeOperation("WRONGNAME", params, "result");
		
		context.set("self", mockVisitor);
		OperatorProvider s = new OperatorProvider();
		OperatorSet opSet = new OperatorSet("MockOperation");
		opSet.addOperator(new MockOperation());
		opSet.setPrefered(MockOperation.class.getName());
		s.addOperationSet(opSet);
		context.set("operators", s);
		assertEquals(false, op.execute(context));
		assertEquals(InvokeException.Type.INTERNAL, op.getLastError().getErrorType());
	}
	
	@Test
	public void testMockOperationExceptionNoSelf() {
		Map<String, Value> params = new HashMap<String, Value>();
		
		Context context = new Context();
		InvokeOperation op = new InvokeOperation("MockOperation", params, "result");
		
		OperatorProvider s = new OperatorProvider();
		OperatorSet opSet = new OperatorSet("MockOperation");
		opSet.addOperator(new MockOperation());
		opSet.setPrefered(MockOperation.class.getName());
		s.addOperationSet(opSet);
		context.set("operators", s);
		assertEquals(false, op.execute(context));
		assertEquals(InvokeException.Type.PARAMETER, op.getLastError().getErrorType());
	}
	
	@Test
	public void testMockOperationSuccessful() {
		Map<String, Value> params = new HashMap<String, Value>();
		
		MockVisitor mockVisitor = new MockVisitor();
		Context context = new Context();
		InvokeOperation op = new InvokeOperation("MockOperation", params, "result");
		
		context.set("self", mockVisitor);
		OperatorProvider s = new OperatorProvider();
		OperatorSet opSet = new OperatorSet("MockOperation");
		opSet.addOperator(new MockOperation());
		opSet.setPrefered(MockOperation.class.getName());
		s.addOperationSet(opSet);
		context.set("operators", s);
		assertEquals(true, op.execute(context));
		assertEquals("", context.get("result"));
	}
}