package net.sf.tweety.logicprogramming.asplibrary.syntax;

/**
 * A Constant is a specialized StringTerm which only allows
 * name with have a lowercase character as first letter.
 * 
 * @author Tim Janus
 */
public class Constant extends StringTerm {

	public Constant(String value) {
		super(value);
	}

	@Override
	public void set(String value) {
		if(value == null || value.length() == 0)
			throw new IllegalArgumentException();
		
		if( !(value.charAt(0) > 96 && value.charAt(0) <= 122))
			throw new IllegalArgumentException("Constant names start with a lower-case character. " +
					"'" + value + "'");
		
		this.name = value;
	}
}

