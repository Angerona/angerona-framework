package net.sf.tweety.util;

import java.util.*;

/**
 * Iterates over all subsets of a given sets.
 * 
 * @author Matthias Thimm
 *
 * @param <T> The element class which is iterated.
 */
public class SubsetIterator<T> implements Iterator<Set<T>> {

	/** The set over which subsets are iterated. */
	private List<T> set;
		
	/** The number of the current item as a bit set. */
	private BitSet currentItem;
		
	/** Creates a new subset iterator for the given set.
	 * @param set some set.
	 */
	public SubsetIterator(Set<T> set){
		this.set = new ArrayList<T>(set);
		this.currentItem = new BitSet(set.size());
	}
	
	/* (non-Javadoc)
	 * @see java.util.Iterator#hasNext()
	 */
	@Override
	public boolean hasNext() {
		return this.currentItem != null;
	}

	/* (non-Javadoc)
	 * @see java.util.Iterator#next()
	 */
	@Override
	public Set<T> next() {
		if(this.currentItem == null)
			throw new NoSuchElementException("No more elements");
		Set<T> result = new HashSet<T>();
		for(int i = 0; i < this.set.size(); i++)
			if(this.currentItem.length() > i && this.currentItem.get(i))
				result.add(this.set.get(i));
		this.currentItem = this.increment(this.currentItem);
		return result;
	}

	/** Increments the given bit set, returns null
	 * if an overflow happens.
	 * @param bitSet some bit set.
	 * @return the incremented bit set
	 */
	private BitSet increment(BitSet bitSet){
		boolean carry = true, tmp;
		int i = 0;
		while(carry){
			tmp = carry;
			carry = carry && bitSet.get(i);
			bitSet.set(i, tmp ^ bitSet.get(i));
			i++;
		}
		if(this.set.size() < i)
			return null;
		return bitSet;
	}
	
	/* (non-Javadoc)
	 * @see java.util.Iterator#remove()
	 */
	@Override
	public void remove() {
		throw new UnsupportedOperationException("This operation is not supported by this class.");		
	}

}
