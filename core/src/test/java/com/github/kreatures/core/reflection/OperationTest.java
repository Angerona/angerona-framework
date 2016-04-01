package com.github.kreatures.core.reflection;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import org.junit.Test;

import com.github.kreatures.core.asml.InvokeOperation;
import com.github.kreatures.core.error.InvokeException;
import com.github.kreatures.core.operators.BaseOperator;
import com.github.kreatures.core.operators.OperatorCallWrapper;
import com.github.kreatures.core.operators.OperatorCaller;
import com.github.kreatures.core.operators.OperatorProvider;
import com.github.kreatures.core.operators.OperatorSet;
import com.github.kreatures.core.operators.OperatorStack;
import com.github.kreatures.core.operators.parameter.GenericOperatorParameter;
import com.github.kreatures.core.operators.parameter.OperatorParameter;
import com.github.kreatures.core.report.Reporter;
import com.github.kreatures.core.util.Pair;

public class OperationTest {
	private static class MockVisitor implements OperatorStack, OperatorCaller {

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
		public Stack<BaseOperator> getOperatorStack() {
			return stack;
		}

		@Override
		public Reporter getReporter() {
			return null;
		}

		@Override
		public OperatorStack getStack() {
			return this;
		}

	}
	
	private class MockOperation implements BaseOperator {

		@Override
		public Pair<String, Class<?>> getOperationType() {
			return new Pair<String, Class<?>>("MockOperation", MockOperation.class);
		}

		@Override
		public Object process(GenericOperatorParameter gop) {
			return "";
		}

		@Override
		public String getPosterName() {
			return "MockOperation";
		}

		@Override
		public Object process(OperatorParameter castParam) {
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
		opSet.addOperator(new OperatorCallWrapper(new MockOperation()));
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
		opSet.addOperator(new OperatorCallWrapper(new MockOperation()));
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
		opSet.addOperator(new OperatorCallWrapper(new MockOperation()));
		opSet.setPrefered(MockOperation.class.getName());
		s.addOperationSet(opSet);
		context.set("operators", s);
		assertEquals(true, op.execute(context));
		assertEquals("", context.get("result"));
	}
}