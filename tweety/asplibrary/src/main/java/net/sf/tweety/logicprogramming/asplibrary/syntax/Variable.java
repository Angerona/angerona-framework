package net.sf.tweety.logicprogramming.asplibrary.syntax;

public class Variable extends Constant {

	public Variable(String value) {
		super(value);
	}

	@Override
	public boolean isVariable() {
		return true;
	}
	
	@Override
	public boolean isConstant() {
		return false;
	}
}
