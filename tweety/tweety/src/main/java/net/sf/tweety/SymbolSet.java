package net.sf.tweety;

import java.util.Set;

/**
 * Provides the structure of a signature or a language as strings and integers.
 * 
 * If you use logical programs but you have to parse FOL formulas this interface
 * helps by translating between the two different signatures.
 * @author Tim Janus
 */
public interface SymbolSet {
	/** the used sort if no sort is given */
	public static final String THING = "Thing";
	
	/** return value for an invalid arity */
	public static final Integer INVALID_ARITY = -1;
	
	/** @return a set of strings representing all constants in a signature. */
	public Set<String> getConstants();
	
	/** @return a set of string representing all variables in a signature. */
	public Set<String> getVariables();
	
	/** @return a set of strings representing all symbols in a signature. */
	public Set<String> getSymbols();
	
	/** @return the arity of the given symbol or INVALID_ARITY if the symbol does not exist. */
	public int getArity(String symbol);
	
	/** @return true if the symbols using sorts for their parameters. */
	public boolean isSorted(); 
	
	/** @return the sort of the given constant or null if the constant does not exist. */
	public String getConstantSort(String constant);
	
	/** @return the sort of given argument of the given symbol or null if either the symbol does not exists or has not so many arguments. */
	public String getSymbolSort(String symbol, int argument);
	
	/** adss the content of the other SymbolSet to this SymbolSet */
	public void add(SymbolSet other);
		
}
