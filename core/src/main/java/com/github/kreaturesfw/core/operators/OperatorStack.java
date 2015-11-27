package com.github.kreaturesfw.core.operators;

import java.util.Stack;

import com.github.kreaturesfw.core.legacy.Operator;

/**
 * The caller of operators must implement this interface
 * which allows the caller to save the stack-trace of the 
 * operators and to use the report mechanism.
 * 
 * @author Tim Janus
 */
public interface OperatorStack {
	/**
	 * Push the given operator on the stack
	 * @param op	reference to an operator.
	 */
	void pushOperator(Operator op);
	
	/** pops the top operator from the stack */
	void popOperator();
	
	/** returns the actual state of the operator stack */
	Stack<Operator> getOperatorStack();
}
