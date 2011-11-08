package net.sf.tweety.logicprogramming.asplibrary.syntax;

/**
 * this interfaces defines common functionality for
 * terms. a term is any construct appearing as an
 * argument in the constructor based definition of
 * a predicate.
 * 
 * @author Thomas Vengels
 *
 */
public interface Term {

	/**
	 * this method returns true whenever the
	 * term is a symbolic constant.
	 * @return
	 */
	public boolean isConstant();
	
	/**
	 * this method returns true whenever the
	 * term is a variable.
	 * @return
	 */
	public boolean isVariable();
	
	/**
	 * this method returns true whenever the
	 * term is an atom. this is true for all
	 * terms appearing at the root-level of
	 * a rule, or for all functional terms.
	 * @return
	 */
	public boolean isAtom();
	
	/**
	 * this method returns true whenever the
	 * term is a list of terms.
	 * @return
	 */
	public boolean isList();
	
	/**
	 * this method returns true whenever the
	 * term is a set of terms.
	 * @return
	 */
	public boolean isSet();
	
	/**
	 * this method returns true whenever the
	 * term is an integer number
	 * @return
	 */
	public boolean isNumber();
	
	/**
	 * this method returns true whenever the
	 * term is an arbitrary string guarded
	 * by '"'. the '"' guards are only emitted
	 * when a term is used in a program output.
	 * @return
	 */
	public boolean isString();

	/**
	 * string value term manipulation
	 * @param value
	 */
	public void set(String value);
	
	/**
	 * returns the textual representation
	 * of the term.
	 * @return
	 */
	public String get();
	
	/**
	 * integer number value manipulation.
	 * only viable for NumberTerm
	 * @param value
	 */
	public void set(int value);
		
	/**
	 * this method returns the integer value
	 * of a term. only viable for NumberTerm
	 * @return
	 */
	public int getInt();
		
}