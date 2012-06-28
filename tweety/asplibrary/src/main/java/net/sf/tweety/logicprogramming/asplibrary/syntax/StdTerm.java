package net.sf.tweety.logicprogramming.asplibrary.syntax;

/**
 * this class models a standard term, which is a term
 * that is not a set, a list or a function. this class
 * is used to model constants and variables. 
 * 
 * @author Thomas Vengels
 *
 */
public class StdTerm implements Term {

	String		termValue;
	int			intValue;
	boolean		isIntType;
	int			hash;
	
	/**
	 * default constructor for string values
	 * 
	 * @param value
	 */
	public StdTerm(String value) {
		this.termValue = value;
		isIntType = false;
		hash = value.hashCode();
	}
	
	/**
	 * default constructor for integer values
	 * @param value
	 */
	public StdTerm(int value) {
		this.intValue = value;
		isIntType = true;
		hash = value;
	}
	
	@Override
	public boolean isConstant() {
		char c0 = termValue.charAt(0);
		return Character.isLowerCase( c0 ) || isIntType;
	}

	@Override
	public boolean isVariable() {
		return Character.isUpperCase( termValue.charAt(0) );
	}
	
	public boolean isBlank() {
		if (termValue == "_")
			return true;
		else
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
	public String toString() {
		String ret = "";
		if (this.termValue != null) {
			// return string value
			ret = this.termValue;
		} else if (this.isIntType) {
			// return int value as string
			ret = ""+this.intValue;
		}
		return ret;
	}

	@Override
	public boolean isNumber() {
		return this.isIntType;
	}

	@Override
	public void set(String value) {
		this.isIntType = false;
		this.termValue = value;
	}

	@Override
	public void set(int value) {
		this.intValue = value;
		this.isIntType = true;
		this.termValue = null;
	}

	@Override
	public String get() {
		return termValue;
	}

	@Override
	public int getInt() {
		return this.intValue;
	}	
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof StdTerm) {
			StdTerm ot = (StdTerm) o;
			if (ot.isIntType && this.isIntType)
				return this.intValue == ot.intValue;
			else if (!ot.isIntType && !ot.isIntType)
				return this.termValue.equals(ot.termValue);
			else
				return false;
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return this.hash;
	}

	@Override
	public boolean isString() {
		return false;
	}

	@Override
	public TermType type() {
		return TermType.Constant;
	}
}
