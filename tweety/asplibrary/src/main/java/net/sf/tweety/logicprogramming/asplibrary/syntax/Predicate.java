package net.sf.tweety.logicprogramming.asplibrary.syntax;

import java.util.LinkedList;
import java.util.List;

import net.sf.tweety.logics.CommonStructure;

/**
 * A predicate has an name and an arity. 
 * @author Tim Janus
 */
public class Predicate implements CommonStructure {

	private String name;
	
	private int arity;
	
	public Predicate(String name) {
		this(name, 0);
	}
	
	public Predicate(String name, int arity) {
		this.name = name;
		this.arity = arity;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getArity() {
		return arity;
	}

	@Override
	public boolean isPredicate() {
		return true;
	}

	@Override
	public boolean isFunctional() {
		return false;
	}

	@Override
	public boolean isSorted() {
		return false;
	}

	@Override
	public List<String> getSorts() {
		List<String> reval = new LinkedList<String>();
		for(int i=0; i<arity; ++i) {
			reval.add(null);
		}
		return reval;
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
