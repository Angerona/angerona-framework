package net.sf.tweety.logicprogramming.asplibrary.syntax;

/**
 * this class extends an ordinary atom to explicitly
 * represent comparisons used in logical rules.
 *  
 * @author Thomas Vengels
 *
 */
public class Relation extends Atom {
	
	public Relation(String op, Term lefthand, Term righthand) {
		super(op,2,lefthand,righthand);
	}
	
	public Term getLefthand() {
		return this.terms[0];
	}
	
	public Term getRighthand() {
		return this.terms[1];
	}
	
	public String getOperator() {
		return this.name;
	}
	
	@Override
	public boolean isRelational() {
		return true;
	}
	
	@Override
	public String toString() {
		String ret = this.terms[0] + " " + this.name + this.terms[1];
		return ret;
	}
}
