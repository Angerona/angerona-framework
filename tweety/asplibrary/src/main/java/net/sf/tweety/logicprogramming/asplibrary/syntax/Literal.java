package net.sf.tweety.logicprogramming.asplibrary.syntax;

/**
 * this interface defines common functionality for
 * an elp literal.
 * 
 * @author Thomas Vengels
 *
 */
public interface Literal {

	/**
	 * returns true if the given literal
	 * is default negated, i.e. an expression
	 * like "not L" for a literal L.
	 * @return
	 */
	public boolean	isDefaultNegated();
	
	/**
	 * indicates if this literal is
	 * true negated.
	 * @return
	 */
	public boolean	isTrueNegated();
	
	
	/**
	 * returns true if this literal
	 * is a condition, i.e. an expression
	 * like "a(X) : b(X)"
	 * @return
	 */
	public boolean	isCondition();
	
	
	/**
	 * indicates if this literal has a weight
	 * assigned to itself.
	 * @return
	 */
	public boolean	isWeightLiteral();
	
	
	/**
	 * this method returns true if the literal
	 * is an aggregate
	 * 
	 * @return true or false
	 */
	public boolean	isAggregate();
	
	
	/**
	 * this method returns true if the literal
	 * is an arithmetic expression.
	 * 
	 * @return true or false
	 */
	public boolean	isArithmetic();
	
	
	/**
	 * this method returns true if the
	 * literal is an relational expressions.
	 * a relational expression consists
	 * of an operator (=,!=,<,>,<=,>=) and
	 * two arguments lOp, rOp, given in
	 * infix notation as "lOp operator rOp".
	 * 
	 * @return true if atom is a relational predicate
	 */
	public boolean	isRelational();
	
	
	/**
	 * returns the atom of this literal.
	 * might be null if this literal is
	 * a condition, or something else
	 * @return
	 */
	public Atom		getAtom();
}
