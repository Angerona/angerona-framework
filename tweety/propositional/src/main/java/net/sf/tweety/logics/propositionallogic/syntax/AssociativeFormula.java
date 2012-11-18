package net.sf.tweety.logics.propositionallogic.syntax;

import java.util.*;


/**
 * This class captures the common functionalities of formulas with an associative
 * operation like conjunction, disjunction, etc.
 *
 * @author Matthias Thimm
 */
public abstract class AssociativeFormula extends PropositionalFormula implements Collection<PropositionalFormula>{

	/**
	 * The inner formulas of this formula 
	 */
	private Set<PropositionalFormula> formulas;
	
	/**
	 * Creates a new associative formula with the given inner formulas. 
	 * @param formulas a collection of formulas.
	 */
	public AssociativeFormula(Collection<? extends PropositionalFormula> formulas){
		this.formulas = new HashSet<PropositionalFormula>(formulas);
	}
	
	/**
	 * Creates a new (empty) associative formula.
	 */
	public AssociativeFormula(){
		this(new HashSet<PropositionalFormula>());
	}
	
	/**
	 * Creates a new associative formula with the two given formulae
	 * @param first a propositional formula.
	 * @param second a propositional formula.
	 */
	public AssociativeFormula(PropositionalFormula first, PropositionalFormula second){
		this();
		this.add(first);
		this.add(second);
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.logics.propositionallogic.syntax.PropositionalFormula#getPropositions()
	 */
	public Set<Proposition> getPropositions(){
		Set<Proposition> propositions = new HashSet<Proposition>();
		for(PropositionalFormula f : this.formulas)
			propositions.addAll(f.getPropositions());		
		return propositions;
	}
	
	/* (non-Javadoc)
	 * @see java.util.Collection#add(java.lang.Object)
	 */
	public boolean add(PropositionalFormula f){
		return this.formulas.add(f);
	}
	
	/* (non-Javadoc)
	 * @see java.util.Collection#addAll(java.util.Collection)
	 */
	public boolean addAll(Collection<? extends PropositionalFormula> c){
		return this.formulas.addAll(c);
	}
	
	/* (non-Javadoc)
	 * @see java.util.Collection#clear()
	 */
	public void clear(){
		this.formulas.clear();
	}
	
	/* (non-Javadoc)
	 * @see java.util.Collection#contains(java.lang.Object)
	 */
	public boolean contains(Object o){
		return this.formulas.contains(o);
	}
	
	/* (non-Javadoc)
	 * @see java.util.Collection#containsAll(java.util.Collection)
	 */
	public boolean containsAll(Collection<?> c){
		return this.formulas.containsAll(c);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((formulas == null) ? 0 : formulas.hashCode());
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
		AssociativeFormula other = (AssociativeFormula) obj;
		if (formulas == null) {
			if (other.formulas != null)
				return false;
		} else if (!formulas.equals(other.formulas))
			return false;
		return true;
	}
	
	/* (non-Javadoc)
	 * @see java.util.Collection#isEmpty()
	 */
	public boolean isEmpty(){
		return this.formulas.isEmpty();
	}
	
	/* (non-Javadoc)
	 * @see java.util.Collection#iterator()
	 */
	public Iterator<PropositionalFormula> iterator(){
		return this.formulas.iterator();
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#remove(java.lang.Object)
	 */
	public boolean remove(Object o){
		return this.formulas.remove(o);
	}
	
	/* (non-Javadoc)
	 * @see java.util.Collection#removeAll(java.util.Collection)
	 */
	public boolean removeAll(Collection<?> c){
		return this.formulas.removeAll(c);
	}
	
	/* (non-Javadoc)
	 * @see java.util.Collection#retainAll(java.util.Collection)
	 */
	public boolean retainAll(Collection<?> c){
		return this.formulas.retainAll(c);
	}
	
	/* (non-Javadoc)
	 * @see java.util.Collection#size()
	 */
	public int size(){
		return this.formulas.size();
	}
	
	/* (non-Javadoc)
	 * @see java.util.Collection#toArray()
	 */
	public Object[] toArray(){
		return this.formulas.toArray();
	}
	
	/* (non-Javadoc)
	 * @see java.util.Collection#toArray(T[])
	 */
	@SuppressWarnings("unchecked")
	public Object[] toArray(Object[] a){
		return this.formulas.toArray(a);
	}

}
