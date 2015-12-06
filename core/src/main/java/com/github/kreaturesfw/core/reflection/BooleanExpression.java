package com.github.kreaturesfw.core.reflection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class evaluates a boolean expression. The expression might be unary, then
 * it is assumed that a Boolean or a reference in the context is given.
 * The expression might also be binary and compare a left and a right value using
 * a specific operator like equal, less etc. 
 * 
 * @author Tim Janus
 */
public class BooleanExpression implements Condition {

	/** reference to the logging facility */
	private static Logger LOG = LoggerFactory.getLogger(Value.class);
	
	/**
	 * Operators for boolean comparing of two values. There is a special
	 * case unary if there are not two values which are compared.
	 * @author Tim Janus
	 */
	public enum Operator {
		
		UNARY		(""),	///> UNARY means there is no compare but the expression is an boolean.
		EQUAL		("=="),	///> EQUAL means both values are tested to be equal.
		NOTEQUAL	("!="),	///> NOTEQUAL means both values are tested to be not equal.
		LESS		("<"),	///> LESS means the check if the left value is lesser than the right value.
		LESSEQ		("<="),	///> LESSEQ means the check if the left value is lesser or equal than the right value.
		GREATER		(">"),	///> GREATER means the check if the left value is greater than the right value.
		GREATEREQ	(">=");	///> GREATEREQ means the check if the left value is greater or equal than the right value.
		
		/** string containing the text representation of the enum */
		private String strOp;
		
		/**
		 * Ctor creating the enum using a string
		 * @param strOp	the string which creates the enum.
		 */
		Operator(String strOp) {
			this.strOp = strOp;
		}
		
		@Override
		public String toString() {
			return strOp;
		}
		
		public static Operator fromString(String strOp) {
			if(strOp != null) {
				for(Operator op : Operator.values()) {
					if(strOp.equalsIgnoreCase(op.toString())) {
						return op;
					}
				}
			}
			return null;
		}
	}
	
	/** the operator used to compare the values left and right */
	private Operator op;
	
	/** the value on the left of the operator or the boolean expression if the operator is UNARY */
	private Value left;
	
	/** the value on the right of the operator or null if the operator is UNARY */
	private Value right;
	
	/**
	 * Ctor: Used for the unary operator case using the given value as boolean expression
	 * @param expression
	 */
	public BooleanExpression(Value expression) {
		this.left = expression;
		this.op = Operator.UNARY;
	}
	
	/**
	 * Ctor: Used for the binary case of a boolean expression in the form: left op right.
	 * @param left	The value on the left of the operator
	 * @param op	The operator 
	 * @param right	The value on the right of the operator
	 */
	public BooleanExpression(Value left, Operator op, Value right) {
		this.left = left;
		this.op = op;
		this.right = right;
	}
	
	@Override
	public boolean evaluate() {
		if(op == Operator.UNARY) {
			Object l = left.getValue();
			if(l instanceof Boolean) {
				return (Boolean)l;
			} else if (left.getType().equals(Value.CONTEXT_REFERENCE_TYPE)) {
				return l != null;
			} else {
				LOG.warn("Unary Boolean Expression uses value of type '{}' returning false.", left.getType());
				return false;
			}
		} else if(op == Operator.EQUAL || op == Operator.NOTEQUAL) {
			boolean reval = false;
			if(left.getValue() == null || right.getValue() == null) {
				reval = left.getValue() == right.getValue();
			} else {
				reval = left.getValue().equals(right.getValue());
			}
			return op == Operator.EQUAL ? reval : !reval;
		} else {
			Double doubleLeft = valueToDouble(left);
			Double doubleRight = valueToDouble(right);
			
			if(doubleLeft == null || doubleRight == null) {
				return false;
				// TODO:
				//throw new AngeronaException("The types of the condition '" + this.toString() + "' are not valid.");
			}
			
			if(op == Operator.LESS) {
				return doubleLeft < doubleRight;
			} else if(op == Operator.LESSEQ) {
				return doubleLeft <= doubleRight;
			} else if(op == Operator.GREATER) {
				return doubleLeft > doubleRight;
			} else if(op == Operator.GREATEREQ) {
				return doubleLeft >= doubleRight;
			}
			
			LOG.error("Operator: '{}' not supported yet. Returning false.", op.toString());
			return false;
		}
	}
	
	private Double valueToDouble(Value val) {
		Double reval = null;
		Object obj = val.getValue();
		if(obj instanceof Integer) {
			reval = ((Integer)obj).doubleValue();
		} else if(obj instanceof Double) {
			reval = (Double)obj;
		}
		return reval;
	}

	@Override
	public void setContext(Context context) {
		if(left!=null)
			left.setContext(context);
		if(right!=null)
			right.setContext(context);
	}
	
	@Override
	public String toString() {
		return left.toString() + (op != Operator.UNARY ? op.toString() + right.toString() : "");
	}
	
	public Value getLeft() {
		return left;
	}
	
	public Value getRight() {
		return right;
	}
	
	public Operator getOperator() {
		return op;
	}

}
