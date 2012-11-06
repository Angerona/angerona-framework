package net.sf.tweety.logicprogramming.asplibrary.syntax;

/**
 * this class extends an ordinary atom to explicitly
 * represent comparisons used in logical rules.
 *  
 * @author Tim Janus
 * @author Thomas Vengels
 *
 */
public class Relation extends Atom {
	
	public Relation(String op, Term<?> lefthand, Term<?> righthand) {
		super(op,lefthand,righthand);
	}
	
	public Term<?> getLefthand() {
		return this.terms.get(0);
	}
	
	public Term<?> getRighthand() {
		return this.terms.get(1);
	}
	
	public String getOperator() {
		return this.getName();
	}
	
	@Override
	public String toString() {
		String ret = this.terms.get(0) + " " + this.getName() + this.terms.get(1);
		return ret;
	}
}
