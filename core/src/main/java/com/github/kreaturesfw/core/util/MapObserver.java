package com.github.kreaturesfw.core.util;

import java.util.Map;

/**
 * 
 * @author Tim Janus
 */
public interface MapObserver {	
	/**
	 * Is called if a put operation happened on an observed map.
	 * @param mapName		The name of the map sending the event, it acts as a property name.
	 * @param changes		A map containing all the changes.
	 */
	<K, V> void onPut(String mapName, Map<K, V> changes);
	
	/**
	 * Is called if a remove operation happened on an observed map.
	 * @param mapName		The name of the map sending the event, it acts as a property name.
	 * @param key			The removed key
	 */
	<K> void onRemove(String mapName, K key);
	
	/**
	 * Is called if a clear operation happend on an observer map.
	 * @param mapName		The name of the map sending the event, it acts as a property name.
	 */
	void onClear(String mapName);
}
