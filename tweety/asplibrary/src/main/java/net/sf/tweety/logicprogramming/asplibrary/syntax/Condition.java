package net.sf.tweety.logicprogramming.asplibrary.syntax;

/**
 * this class models a condition. a condition
 * is an expressen "A : B" of two literals A
 * and B. conditions can be grounded to either
 * disjunctions or conjunctions of literal.
 * 
 * note: gringo specific
 * note: not really supported in this release 
 * 
 * @author Thomas Vengels
 *
 */
public class Condition implements Literal {

	Literal		lArg,rArg;
	
	public Condition(Literal left, Literal right) {
		this.lArg = left;
		this.rArg = right;
	}
	
	public Literal getLeftArg() {
		return lArg;
	}
	
	public Literal getRightArg() {
		return rArg;
	}
	
	@Override
	public String toString() {
		return lArg + " : " + rArg;
	}

	@Override
	public boolean isDefaultNegated() {
		return false;
	}

	@Override
	public boolean isTrueNegated() {
		return false;
	}

	@Override
	public boolean isCondition() {
		return true;
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
		return null;
	}

	@Override
	public boolean isArithmetic() {
		return false;
	}

	@Override
	public boolean isRelational() {
		return false;
	}

	@Override
	public boolean isGround() {
		return lArg.isGround() && rArg.isGround();
	}
}
