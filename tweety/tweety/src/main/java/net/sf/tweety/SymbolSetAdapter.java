package net.sf.tweety;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Basic implementation of the SymbolSet interface.
 * 
 * Use specific subclasses to translate a signature.
 * @author Tim Janus
 *
 */
public abstract class SymbolSetAdapter implements SymbolSet {

	/** flag indicating if the symbol set is sorted */
	private boolean sorted;
	
	/** set of strings naming the constants of the SymbolSet */
	protected Set<String> constants = new HashSet<String>();
	
	/** set of strings naming the symbols of the SymbolSet */
	protected Set<String> symbols = new HashSet<String>();
	
	/** set of strings naming the variables of the SymbolSet */
	protected Set<String> variables = new HashSet<String>();
	
	/** maps symbols to their list of sorts (arguments). Is null if sorted flag is false. */
	protected Map<String, List<String>> symbolSorts;
	
	/** maps constant to their sort. Is null if sorted flag is false. */
	protected Map<String, String> constantSorts;
	
	/** maps symbols to their arity. Is null if sorted flag is true. */
	protected Map<String, Integer> aritys;
	
	/**
	 * Ctor: called by subclasses to decide if the SymbolSet is sorted or not.
	 * @param sorted
	 */
	public SymbolSetAdapter(boolean sorted) {
		this.sorted = sorted;
		if(sorted) {
			symbolSorts = new HashMap<String, List<String>>();
			constantSorts = new HashMap<String, String>();
		}
		else
			aritys = new HashMap<String, Integer>();
	}
	
	/**
	 * Fills the SymbolSet with the language structure of the given signature.
	 * @param sig The signature with the language constructs to translate into the generic SymbolSet.
	 */
	public abstract void fromSignature(Signature sig);
	
	@Override
	public Set<String> getConstants() {
		return constants;
	}

	@Override
	public Set<String> getVariables() {
		return variables;
	}
	
	@Override
	public Set<String> getSymbols() {
		return symbols;
	}

	@Override
	public int getArity(String symbol) {
		if(sorted) {
			if(!symbolSorts.containsKey(symbol))	return -1;
			return symbolSorts.get(symbol).size();
		}
		if(!aritys.containsKey(symbol))				return -1;
		return aritys.get(symbol);
	}

	@Override
	public boolean isSorted() {
		return sorted;
	}

	@Override
	public String getConstantSort(String constant) {
		if(sorted) {
			if(!constantSorts.containsKey(constant))	return null;
			return constantSorts.get(constant);
		}
		return THING;
	}

	@Override
	public String getSymbolSort(String symbol, int argument) {
		if(sorted) {
			if(!symbolSorts.containsKey(symbol)) 			return null;
			if(argument >= symbolSorts.get(symbol).size())	return null;
			return symbolSorts.get(symbol).get(argument);
		}
		return THING;
	}

}
