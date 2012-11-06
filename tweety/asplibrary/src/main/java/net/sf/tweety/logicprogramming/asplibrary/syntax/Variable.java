package net.sf.tweety.logicprogramming.asplibrary.syntax;

/**
 * A Variable is a specialized StringTerm which only allows
 * name with have a uppercase character as first letter.
 * 
 * @author Tim Janus
 */
public class Variable extends StringTerm {

	public Variable(String value) {
		super(value);
	}

	@Override
	public void set(String value) {
		if(value == null || value.length() == 0)
			throw new IllegalArgumentException();
		
		if( !(value.charAt(0) > 64 && value.charAt(0) < 90) && value.charAt(0) != '_')
			throw new IllegalArgumentException("Variable names start with a upper-case character." +
					"'" + value +"'");
		
		this.name = value;
	}
}
