package net.sf.tweety.logicprogramming.asplibrary.syntax;

/**
 * this class extends an ordinary atom to be used as
 * an arithmetic expression.
 * 
 * @author Thomas Vengels
 *
 */
public class Arithmetic extends Atom {
	
	public Arithmetic(String op, Term Arg1, Term Arg2, Term Result) {
		super(op,3,Arg1,Arg2,Result);
	}
	
	public Term getResult() {
		return this.terms[2];
	}
	
	public Term getFirstArgument() {
		return this.terms[0];
	}
	
	public Term getSecondArgument() {
		return this.terms[1];
	}
	
	public String getOperator() {
		return this.name;
	}
	
	@Override
	public String toString() {
		String ret = terms[2] + " = " + this.terms[0] + " " + this.name + " "
			+ this.terms[1];
		
		return ret;
	}
}
