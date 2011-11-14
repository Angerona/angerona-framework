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
public class StringTerm extends Constant {

	public StringTerm(String value) {
		super(fixMarks(value));
	}
	
	protected static String fixMarks(String s) {
		return s;
	}
	
	@Override
	public boolean isConstant() {
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
	public String toString() {
		return this.value;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof StringTerm) {
			StringTerm st = (StringTerm) o;
			return st.value.equals( this.value );
		} else
			return false;
	}

	@Override
	public TermType type() {
		return TermType.Stirng;
	}
}
