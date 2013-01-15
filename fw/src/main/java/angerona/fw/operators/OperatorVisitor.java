package angerona.fw.operators;

import java.util.Stack;



/**
 * The owner of an operator must implement this interface
 * which allows the owner to save the stack-trace of the 
 * operators.
 * @author Tim Janus
 */
public interface OperatorVisitor {
	/**
	 * Push the given operator on the stack
	 * @param op	reference to an operator.
	 */
	void pushOperator(BaseOperator op);
	
	/** pops the top operator from the stack */
	void popOperator();
	
	/** returns the actual state of the operator stack */
	Stack<BaseOperator> getStack();
}
