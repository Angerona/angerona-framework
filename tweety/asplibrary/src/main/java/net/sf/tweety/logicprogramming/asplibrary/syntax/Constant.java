package net.sf.tweety.logicprogramming.asplibrary.syntax;

public class Constant implements Term {

	protected String value = null;
	
	public Constant(String value) {
		this.value = value;
	}
	
	@Override
	public boolean isConstant() {
		return true;
	}

	@Override
	public boolean isVariable() {
		return false;
	}

	@Override
	public boolean isAtom() {
		return false;
	}

	@Override
	public boolean isList() {
		return false;
	}

	@Override
	public boolean isSet() {
		return false;
	}

	@Override
	public boolean isNumber() {
		return false;
	}

	@Override
	public boolean isString() {
		return false;
	}

	@Override
	public void set(String value) {
		this.value = value;
	}

	@Override
	public String get() {
		return value;
	}

	@Override
	public void set(int value) {
		this.value = "" + value;
	}

	@Override
	public int getInt() {
		return 0;
	}

	@Override
	public String toString() {
		return this.value;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Constant) {
			Constant co = (Constant) o;
			return co.value.equals( this.value );
		} else
			return false;
	}

	@Override
	public TermType type() {
		return TermType.Constant;
	}

}

