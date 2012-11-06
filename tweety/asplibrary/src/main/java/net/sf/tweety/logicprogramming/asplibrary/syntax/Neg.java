package net.sf.tweety.logicprogramming.asplibrary.syntax;

/**
 * This class models strict negation for atoms.
 * 
 * @author Tim Janus
 * @author Thomas Vengels
 */
public class Neg implements Literal {

	Atom	atom;
	
	public Neg(Atom inner) {
		this.atom = inner;
	}

	@Override
	public Atom getAtom() {
		return this.atom;
	}
	
	@Override
	public String toString() {
		return "-" + this.atom;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Neg) {
			Neg on = (Neg) o;
			
			// compare atom
			return on.getAtom().equals( this.getAtom() );
		} else {
			return false;
		}
	}

	@Override
	public boolean isGround() {
		return atom.isGround();
	}
}
