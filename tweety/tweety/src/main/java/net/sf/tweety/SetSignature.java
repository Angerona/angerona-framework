package net.sf.tweety;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.sf.tweety.logics.CommonStructure;
import net.sf.tweety.logics.CommonTerm;

/**
 * This class models a signature as a set of formulas.
 * 
 * @author Matthias Thimm
 *
 * @param T The type of formulas in this signature.
 */
public class SetSignature<T extends Formula> extends Signature implements Collection<T> {

	private Set<T> formulas;
	
	/**
	 * Creates a empty new set signature.
	 */
	public SetSignature(){
		this(new HashSet<T>());
	}
	
	/**
	 * Creates a new set signature with the single given formula.
	 * @param f a formula.
	 */
	public SetSignature(T f){
		this(new HashSet<T>());
		this.add(f);
	}
	
	/**
	 * Creates a new set signature with the given set of formulas.
	 * @param formulas a collection of formulas.
	 */
	public SetSignature(Collection<? extends T> formulas){
		this.formulas = new HashSet<T>(formulas);
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.kr.Signature#isSubSignature(net.sf.tweety.kr.Signature)
	 */
	public boolean isSubSignature(Signature other){
		if(!(other instanceof SetSignature<?>))
			return false;
		return ((SetSignature<?>)other).containsAll(this);
	}
	
	/* (non-Javadoc)
	 * @see net.sf.tweety.Signature#addSignature(net.sf.tweety.Signature)
	 */
	@SuppressWarnings("unchecked")
	public void addSignature(Signature other){
		if(!(other instanceof SetSignature<?>))
			throw new IllegalArgumentException("The given object is no set signature.");
		this.formulas.addAll(((SetSignature<T>)other).formulas);		
	}
		
	/* (non-Javadoc)
	 * @see java.util.Collection#add(java.lang.Object)
	 */
	@Override
	public boolean add(T e) {
		return this.formulas.add(e);
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#addAll(java.util.Collection)
	 */
	@Override
	public boolean addAll(Collection<? extends T> c) {
		return this.formulas.addAll(c);
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#clear()
	 */
	@Override
	public void clear() {
		this.formulas.clear();
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#contains(java.lang.Object)
	 */
	@Override
	public boolean contains(Object o) {
		return this.formulas.contains(o);
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#containsAll(java.util.Collection)
	 */
	@Override
	public boolean containsAll(Collection<?> c) {
		return this.formulas.containsAll(c);
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#isEmpty()
	 */
	@Override
	public boolean isEmpty() {
		return this.formulas.isEmpty();
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#iterator()
	 */
	@Override
	public Iterator<T> iterator() {
		return this.formulas.iterator();
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#remove(java.lang.Object)
	 */
	@Override
	public boolean remove(Object o) {
		return this.formulas.remove(o);
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#removeAll(java.util.Collection)
	 */
	@Override
	public boolean removeAll(Collection<?> c) {
		return this.formulas.removeAll(c);
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#retainAll(java.util.Collection)
	 */
	@Override
	public boolean retainAll(Collection<?> c) {
		return this.formulas.retainAll(c);
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#size()
	 */
	@Override
	public int size() {
		return this.formulas.size();
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#toArray()
	 */
	@Override
	public Object[] toArray() {
		return this.formulas.toArray();
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#toArray(T[])
	 */
	@Override
	public <S> S[] toArray(S[] a) {
		return this.formulas.toArray(a);
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
		SetSignature<?> other = (SetSignature<?>) obj;
		if (formulas == null) {
			if (other.formulas != null)
				return false;
		} else if (!formulas.equals(other.formulas))
			return false;
		return true;
	}

	@Override
	public void fromSymbolSet(SymbolSet symbols) {
		// TODO does not supports the symbol translation, cant see an easy way here.
	}

	@Override
	public Set<CommonStructure> getCommonStructureElements() {
		// TODO does not supports the symbol translation, cant see an easy way here.
		return null;
	}

	@Override
	public Set<CommonTerm> getCommonTermElements() {
		// TODO does not supports the symbol translation, cant see an easy way here.
		return null;
	}

	@Override
	public void fromSignature(Signature signature) {
		// TODO does not supports the symbol translation, cant see an easy way here.
	}

}
