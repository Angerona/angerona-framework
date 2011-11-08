package net.sf.tweety.logics;

import java.util.List;

public interface CommonStructure {
	
	/** @return the name of the predicate */
	String getName();
	
	/** @return the arity of the predicate */
	int getArity();
	
	/** @return true if the structure represents a predicate. */
	boolean isPredicate();
	
	/** @return true if the structure represents a functional. */
	boolean isFunctional();
	
	/** @return true if the predicate implementations supports sorted arguments otherwise false. */
	boolean isSorted();
	
	/** @return a list of String representing the sorts of the list, the size of this list should be the nbumber returned by getArtiy() */
	List<String> getSorts();
}
