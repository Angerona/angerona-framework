package net.sf.tweety.logicprogramming.asplibrary.syntax;

/**
 * this class extends an ordinary atom to be used as
 * an arithmetic expression.
 * 
 * @author Thomas Vengels
 *
 */
public class Arithmetic extends Atom {
	
	public Arithmetic(String op, Term<?> Arg1, Term<?> Arg2, Term<?> Result) {
		super(op,Arg1,Arg2,Result);
	}
	
	public Term<?> getResult() {
		return this.terms.get(2);
	}
	
	public Term<?> getFirstArgument() {
		return this.terms.get(0);
	}
	
	public Term<?> getSecondArgument() {
		return this.terms.get(1);
	}
	
	public String getOperator() {
		return getName();
	}
	
	@Override
	public String toString() {
		String ret = terms.get(2) + " = " + this.terms.get(0) + " " + getName() + " "
			+ this.terms.get(1);
		
		return ret;
	}
}
