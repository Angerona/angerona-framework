package com.github.angerona.fw.motivation.plans;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 
 * @author Manuel Barbi
 *
 */
public class Trail {

	private Map<String, TrailElem[]> items = new LinkedHashMap<>();

	public void put(String location, TrailElem[] elems) {
		items.put(location, elems);
	}

	public TrailElem[] get(String location) {
		return items.get(location);
	}

}
