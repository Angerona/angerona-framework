package com.github.kreatures.core.comp;

import java.util.LinkedList;
import java.util.List;

/**
 * Presentable provides methods to automatically create views for agent-components
 * 
 * @author Manuel Barbi
 *
 */
public interface Presentable {

	default List<String> getRepresentation() {
		List<String> representation = new LinkedList<>();
		getRepresentation(representation);
		return representation;
	}

	/**
	 * fill the list with for example one String per item
	 * 
	 * @param representation
	 */
	void getRepresentation(List<String> representation);

}
