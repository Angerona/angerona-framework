package net.sf.tweety.logics;

/**
 * Defines a common interface for terms. Sublanguages of tweety
 * should implement this interface to support Interoperability 
 * @author Tim Janus
 */
public interface CommonTerm {
	
	/** @return the name of the term */
	String getName();
	
	/** return the sort (type) of this term */
	String getSortName();
	
	/** @return true if the term is a variable otherwise false. */
	boolean isVariable();
	
	/** @return true if the term is a constant otherwise false. */
	boolean isConstant();
	
	/** @return true if the term is a number otherwise false. */
	boolean isNumber();
}
