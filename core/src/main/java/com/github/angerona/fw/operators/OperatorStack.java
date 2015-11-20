package com.github.angerona.fw.operators;

import java.util.Stack;

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
	void pushOperator(BaseOperator op);
	
	/** pops the top operator from the stack */
	void popOperator();
	
	/** returns the actual state of the operator stack */
	Stack<BaseOperator> getOperatorStack();
}
