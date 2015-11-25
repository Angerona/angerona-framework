package com.github.kreatures.core.util;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * MultiMapObservable is a support class, it implements
 * the features of MapObservableSupport and adds the ability to register/unregister maps.
 * So it can be used by a Model class to register multiple maps which are observed
 * by the Observers of the model.
 * 
 * @author Tim Janus
 */
public final class MultiMapObservable implements MapObservable, MapObserver {
	/** used as implementation of the MapObservable features */
	private MapObservableSupport observableSupport = new MapObservableSupport();

	/** The set contains all the observable Maps */
	private Set<ObservableMap<?, ?>> maps = new HashSet<>();
	
	public void registerMap(ObservableMap<?, ?> map) {
		if(!observableSupport.isEventPropagating()) {
			maps.add(map);
			map.addMapObserver(this);
		}
	}
	
	public void unregisterMap(ObservableMap<?, ?> map) {
		if(!observableSupport.isEventPropagating()) {
			if(maps.remove(map)) {
				map.removeMapObserver(this);
			}
		}
	}
	
	@Override
	public void addMapObserver(MapObserver observer) {
		observableSupport.addMapObserver(observer);
	}

	@Override
	public void removeMapObserver(MapObserver observer) {
		observableSupport.removeMapObserver(observer);
	}

	@Override
	public <K, V> void onPut(String mapName, Map<K, V> changes) {
		observableSupport.onPut(mapName, changes);
	}

	@Override
	public <K> void onRemove(String mapName, K key) {
		observableSupport.onRemove(mapName, key);
	}

	@Override
	public void onClear(String mapName) {
		observableSupport.onClear(mapName);
	}
}
