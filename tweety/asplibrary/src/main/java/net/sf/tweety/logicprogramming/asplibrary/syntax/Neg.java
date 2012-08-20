package net.sf.tweety.logicprogramming.asplibrary.syntax;

/**
 * this class models strict negation for atoms.
 * 
 * @author Thomas Vengels
 *
 */
public class Neg implements Literal {

	Atom	atom;
	
	public Neg(Atom inner) {
		this.atom = inner;
	}
	
	@Override
	public boolean isDefaultNegated() {
		return false;
	}

	@Override
	public boolean isTrueNegated() {
		return true;
	}

	@Override
	public boolean isCondition() {
		return false;
	}

	@Override
	public boolean isWeightLiteral() {
		return false;
	}

	@Override
	public boolean isAggregate() {
		return false;
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
	public boolean isArithmetic() {
		// arithmetic terms cannot be truely negated
		return false;
	}

	@Override
	public boolean isRelational() {
		// arithmetic terms cannot be default negated
		return false;
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
