package net.sf.tweety.logics.firstorderlogic.syntax;

import java.util.List;

/**
 * A predicate in first-order logic, i.e. an identifier for a relation between objects.
 * @author Matthias Thimm
 */
public class Predicate extends FolBasicStructure  {
	
	/**
	 * Initializes a predicate of arity zero with the given name; 
	 * @param name the name of the predicate
	 */
	public Predicate(String name){
		super(name);	
	}
	/**
	 * Initializes a predicate with the given name and of the given arity.
	 * Every argument gets the sort Sort.THING. 
	 * @param name the name of the predicate
	 */
	public Predicate(String name, int arity){
		super(name,arity);
	}
	
	
	/**
	 * Initializes a predicate with the given name and the given list
	 * of argument sorts.
	 * @param name the name of the predicate
	 * @param arguments the sorts of the arguments
	 */
	public Predicate(String name, List<Sort> arguments){
		super(name,arguments);
	}
	
	/**
	 * Appends the given sort to this predicate's
	 * arguments and returns itself.
	 * @param sort a sort to be added
	 * @return the predicate itself.
	 */
	public Predicate addArgument(Sort sort){
		List<Sort> arguments = this.getArguments();		
		arguments.add(sort);
		this.setArguments(arguments);
		return this;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolBasicStructure#hashCode()
	 */
	public int hashCode(){
		return super.hashCode()+7;
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.firstorderlogic.syntax.FolBasicStructure#equals(java.lang.Object)
	 */
	public boolean equals(Object obj){
		if (this == obj)
			return true;		
		if (getClass() != obj.getClass())
			return false;		
		return super.equals(obj);
	}

}
