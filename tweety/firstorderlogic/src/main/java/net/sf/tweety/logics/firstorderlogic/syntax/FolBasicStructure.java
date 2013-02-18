package net.sf.tweety.logics.firstorderlogic.syntax;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * The abstract parent for predicates and functors. This class captures their common
 * functionalities.
 * 
 * @author Matthias Thimm
 *
 */
public abstract class FolBasicStructure {
	/**
	 * The name of this structure
	 */
	private String name;
	
	/**
	 * This list constrains the possible arguments of this structure
	 * to the given sorts. Therefore the arity of this structure is
	 * the size of the list.
	 */
	private List<Sort> arguments;
	
	/**
	 * Initializes a structure of arity zero with the given name; 
	 * @param name the name of the structure
	 */
	public FolBasicStructure(String name){
		this.name = name;
		this.arguments = new ArrayList<Sort>();
	}
	
	/**
	 * Initializes a structure with the given name and of the given arity.
	 * Every argument gets the sort Sort.THING. 
	 * @param name the name of the structure
	 */
	public FolBasicStructure(String name, int arity){
		this(name);
		for(int i = 0; i < arity; i++)
			this.arguments.add(Sort.THING);
	}
	
	/**
	 * Initializes a structure with the given name and the given list
	 * of argument sorts.
	 * @param name the name of the structure
	 * @param arguments the sorts of the arguments
	 */
	public FolBasicStructure(String name, List<Sort> arguments){
		this(name);
		this.arguments.addAll(arguments);
	}
	
	/**
	 * Returns the name of this structure
	 * @return the name of this structure
	 */
	public String getName(){
		return new String(this.name);
	}
	
	/**
	 * Returns the arguments of this structure (the sorts
	 * of the parameters)
	 * @return  the arguments of this structure (the sorts
	 * of the parameters)
	 */
	public List<Sort> getArguments(){
		return new ArrayList<Sort>(this.arguments);
	}
	
	/**
	 * Sets the argument list of this structure
	 * @param arguments a list of sorts as arguments
	 */
	protected void setArguments(List<Sort> arguments){
		this.arguments = arguments;
	}
	
	/**
	 * Returns the arity of this structure
	 * @return the arity of this structure
	 */
	public int getArity(){
		return this.arguments.size();
	}	

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString(){
		String s = this.name;
		/*Iterator<Sort> it = this.arguments.iterator();
		if(!it.hasNext())
			return s;
		s += "(" + it.next();
		while(it.hasNext())
			s += "," + it.next();
		s += ")";*/		
		return s;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((arguments == null) ? 0 : arguments.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FolBasicStructure other = (FolBasicStructure) obj;
		if (arguments == null) {
			if (other.arguments != null)
				return false;
		} else if (!arguments.equals(other.arguments))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	public List<String> getSorts() {
		List<String> reval = new LinkedList<String>();
		for(Sort s : arguments) {
			reval.add(s.getName());
		}
		return reval;
	}
}
