package net.sf.tweety.logicprogramming.asplibrary.syntax;

/**
 * This class represents terms which are objects identified by a
 * string. Subclasses are Variable and Constant.
 * 
 * @author Tim Janus
 * @author Thomas Vengels
 *
 */
public abstract class StringTerm implements Term<String> {
	/** the name for the term */
	protected String name;
	
	public StringTerm(String value) {
		set(value);
	}
	
	@Override
	public abstract void set(String value);

	@Override
	public String get() {
		return this.name;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof StringTerm) {
			StringTerm st = (StringTerm) o;
			return st.name.equals( this.name );
		} else
			return false;
	}
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}
}
