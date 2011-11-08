package net.sf.tweety.logicprogramming.asplibrary.syntax;

/**
 * this class holds some additional directives
 * like DLVs #maxint statements
 *  
 * note: might use some fine tuning. support
 * by the parser and solver interface unknown.
 *  
 * @author Thomas Vengels
 *
 */
public class Preamble {

	public Preamble() {
		
	}
	
	public void join(Preamble other) {
		if (other.maxInt > this.maxInt)
			this.maxInt = other.maxInt;
	}
	
	public int		maxInt = 0;
}
