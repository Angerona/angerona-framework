package net.sf.tweety.logicprogramming.asplibrary.syntax;

/**
 * this class represents a string term, which is
 * used to declare arbitrary strings as terms. the
 * string is guarded by '"', an d comparison to other
 * term types always fails.
 * 
 * @author Thomas Vengels
 *
 */
public class StringTerm implements Term {

	protected String value = null;
	
	@Override
	public boolean isConstant() {
		return false;
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
		return true;
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
		return "\"" + this.value + "\"";
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof StringTerm) {
			StringTerm st = (StringTerm) o;
			return st.value.equals( this.value );
		} else
			return false;
	}
}
