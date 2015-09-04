package com.github.angerona.fw.comp;

import java.util.LinkedList;
import java.util.List;

/**
 * Presentable provides methods to automatically create views for agent-components
 * 
 * @author Manuel Barbi
 *
 */
public interface Presentable {

	default List<String> present() {
		List<String> representation = new LinkedList<>();
		present(representation);
		return representation;
	}

	void present(List<String> representation);

}
