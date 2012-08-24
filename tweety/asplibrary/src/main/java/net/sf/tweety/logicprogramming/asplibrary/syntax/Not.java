package net.sf.tweety.logicprogramming.asplibrary.syntax;

/**
 * this class models a default negated literal. in answer set
 * programming, the body of a rule is usually composed of a
 * set of positive and negative literals, where this valuation
 * refers to default negation or negation as failure. when
 * implementing a rule, there are two opportunities:
 * - implement the rule with two distinct lists, representing
 *   the sets of positive and negative literals
 * - implement the rule with one set containing super literals,
 *   where a super literal can be positive or strictly negated,
 *   with or without default negation.
 * the library takes the second approach, which allows more
 * flexibility, but comes at the cost that malformed constructs
 * like "not not a" are not intercepted by the library.
 * 
 * @author Thomas Vengels
 *
 */
public class Not implements Literal {

	Literal		lit;

	public Not(Literal inner) {
		this.lit = inner;		
	}
	
	@Override
	public boolean isDefaultNegated() {
		return true;
	}

	@Override
	public boolean isTrueNegated() {
		return lit.isTrueNegated();
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
		return lit.isAggregate();
	}

	@Override
	public Atom getAtom() {
		return lit.getAtom();
	}

	@Override
	public String toString() {
		return "not " + this.lit;
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
	public boolean equals(Object o) {
		if (o instanceof Not) {
			Not on = (Not) o;
			return on.lit.equals(this.lit);
		} else {
			return false;
		}
	}

	@Override
	public boolean isGround() {
		return lit.isGround();
	}
}
