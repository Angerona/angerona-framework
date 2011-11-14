package net.sf.tweety.logicprogramming.asplibrary.factory;

import net.sf.tweety.logicprogramming.asplibrary.syntax.*;

/**
 * this class models a refelction class for terms
 * used by the asp library
 * 
 * @author Thomas Vengels
 *
 */
public class TermDescriptor {

	public	TermType	type;
	public	String		className;
	
	public	TermDescriptor(TermType t, String cn) {
		this.type = t;
		this.className = cn;
	}
}
