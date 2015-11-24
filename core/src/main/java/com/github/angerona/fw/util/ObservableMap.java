package com.github.angerona.fw.util;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Implemetns the Map interface by using a HashMap as implementation and adds
 * event methods which inform observers about changes of the map.
 * @author Tim Janus
 *
 * @param <K>	Type of the keys.
 * @param <V>	Type of the values
 */
public class ObservableMap<K, V> implements Map<K, V>, MapObservable {

	/** the name of the map to identify it in the event mechanism */
	private String name;
	
	/** the map used as map implementation, as default it is a HashMap */
	private Map<K,V> impl;
	
	/** the implementation of a ObservableMap */
	private MapObservableSupport support = new MapObservableSupport();
	
	public ObservableMap(String mapName) {
		this.impl = new HashMap<>();
		this.name = mapName;
	}
	
	public ObservableMap(String mapName, Map<K, V> implementation) {
		if(implementation==null)
			throw new IllegalArgumentException("implementation must not be null.");
		this.impl = implementation;
		this.name = mapName;
	}
	
	@Override
	public V put(K key, V value) {
		Map<K, V> changes = new HashMap<>();
		changes.put(key, value);
		V reval = impl.get(key);
		
		support.onPut(name, Collections.unmodifiableMap(changes));
		impl.put(key, value);
		return reval;
	}
	
	@Override
	public V remove(Object key) {
		if(impl.containsKey(key)) {
			support.onRemove(name, key);
			return impl.remove(key);
		}
		return null;
	}
	
	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		support.onPut(name, Collections.unmodifiableMap(m));
		impl.putAll(m);
	}
	
	@Override
	public void clear() {
		support.onClear(name);
		impl.clear();
	}

	@Override
	public boolean containsKey(Object key) {
		return impl.containsKey(key);
	}

	@Override
	public boolean containsValue(Object value) {
		return impl.containsValue(value);
	}

	@Override
	public Set<java.util.Map.Entry<K, V>> entrySet() {
		return impl.entrySet();
	}

	@Override
	public V get(Object key) {
		return impl.get(key);
	}

	@Override
	public boolean isEmpty() {
		return impl.isEmpty();
	}

	@Override
	public Set<K> keySet() {
		return impl.keySet();
	}

	@Override
	public int size() {
		return impl.size();
	}

	@Override
	public Collection<V> values() {
		return impl.values();
	}

	@Override
	public void addMapObserver(MapObserver observer) {
		support.addMapObserver(observer);
	}

	@Override
	public void removeMapObserver(MapObserver observer) {
		support.removeMapObserver(observer);
	}

}
