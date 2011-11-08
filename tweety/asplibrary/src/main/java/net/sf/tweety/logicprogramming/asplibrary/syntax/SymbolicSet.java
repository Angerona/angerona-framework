package net.sf.tweety.logicprogramming.asplibrary.syntax;

import java.util.*;

/**
 * this class is used to store symbolic sets.
 *  
 * @author Thomas Vengels
 *
 */
public class SymbolicSet {
	
	protected Set<String> vars;
	protected Collection<Literal> lits;
	
	public SymbolicSet(Set<String> variables, Collection<Literal> literals ) {
		this.vars = variables;
		this.lits = literals;
	}
	
	public Set<String> getVariables() {
		return this.vars;
	}
	
	@Override
	public String toString() {
		String ret = "{";
		
		Iterator<String> sIter = this.vars.iterator();
		
		if (sIter.hasNext()) {
			ret += sIter.next();
			
			while(sIter.hasNext()) {
				ret += ", "+sIter.next();
			}
		}
		
		ret += ":";
		
		Iterator<Literal> lIter = lits.iterator();
		
		if (lIter.hasNext()) {
			ret += lIter.next();
			
			while(lIter.hasNext())
				ret += ", " + lIter.next();
		}
		
		ret += "}";
		
		return ret;
	}
}
