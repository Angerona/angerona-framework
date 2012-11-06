package net.sf.tweety.logicprogramming.asplibrary.syntax;

/**
 * this class represents an aggregate function. aggregates
 * are functions like sum, times, count over a symbolic set,
 * a set of literals and local variables.
 * 
 * @author Thomas Vengels
 *
 */
public class Aggregate extends Atom {

	public Aggregate(String functor, SymbolicSet symSet) {
		super(functor);
		this.symSet = symSet;
	}
	
	protected SymbolicSet	symSet = null;
	protected Term<?> leftGuard = null, rightGuard = null;
	protected String leftRel = null, rightRel = null;
	
	public boolean	hasLeftGuard() {
		return	leftGuard != null;
	}
	
	public boolean hasRightGuard() {
		return	rightGuard != null;
	}
	
	public Term<?>	getLeftGuard() {
		return leftGuard;
	}
	
	public String getLeftRel() {
		return leftRel;
	}
	
	public Term<?> getRightGuard() {
		return rightGuard;
	}
	
	public String getRightRel() {
		return rightRel;
	}
	
	public void setLeftGuard(Term<?> guard, String rel) {
		this.leftGuard = guard;
		this.leftRel = rel;
	}
	
	public void setRightGuard(Term<?> guard, String rel) {
		this.rightGuard = guard;
		this.rightRel = rel;
	}
	
	public SymbolicSet getSymbolicSet() {
		return this.symSet;
	}
	
	@Override
	public String toString() {
		String ret = "";
		if (this.leftGuard != null) {
			ret += this.leftGuard + " " + this.leftRel + " ";
		}
		ret += getName() + this.symSet;
		if (this.rightGuard != null) {
			ret += " " + this.rightRel + " " + this.rightGuard;
		}
		return ret;
	}
}
