package net.sf.tweety.logicprogramming.asplibrary.syntax;

/**
 * This interface defines common functionality for
 * an elp literal.
 * 
 * @author Tim Janus
 *
 */
public interface Literal {

	/**
	 * Gets the atom of this literal.
	 * might be null if this literal is
	 * a condition, or something else
	 * @return
	 */
	public Atom		getAtom();
	
	/**
	 * returns if the atom is grounded or has variables.
	 * @return	true if no variables are bound in the literal, false otherwise.
	 */
	public boolean isGround();
}
