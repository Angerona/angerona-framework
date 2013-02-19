package net.sf.tweety.logicprogramming.asplibrary.syntax;


/**
 * A predicate has a name and an arity. 
 * @author Tim Janus
 */
public class Predicate {

	private String name;
	
	private int arity;
	
	public Predicate(String name) {
		this(name, 0);
	}
	
	public Predicate(String name, int arity) {
		this.name = name;
		this.arity = arity;
	}
	
	public String getName() {
		return name;
	}

	public int getArity() {
		return arity;
	}


	@Override
	public boolean equals(Object other) {
		if(other instanceof Predicate) {
			Predicate o = (Predicate)other;
			return name.equals(o.name) && arity == o.arity;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return name.hashCode() + arity;
	}
}
