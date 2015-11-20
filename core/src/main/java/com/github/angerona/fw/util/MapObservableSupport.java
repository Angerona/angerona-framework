package com.github.angerona.fw.util;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * This support class implements the functionality to handle multiple MapObserver,
 * those Observer can be register/unregistred and their event methods can be
 * invoked. 
 * The class support the adding of new observers during event propagation therefore
 * it buffers the add and remove operations and executes them after the event 
 * propagation.
 * The class is not thread safe.
 * @author Tim Janus
 */
public final class MapObservableSupport implements MapObservable, MapObserver {

	/** indicates if a event propagation is happening */
	private boolean flagEventPropagation = false;
	
	/** the set of registered observers */
	private Set<MapObserver> observers = new HashSet<>();
	
	/** set contains observers which have to be added after event propagation */
	private Set<MapObserver> toAdd = new HashSet<>();
	
	/** set contains observers which have to be removed after event propagation */
	private Set<MapObserver> toRemove = new HashSet<>();
	
	/** @return a flag indicating if the event propagation is currently running */
	public boolean isEventPropagating() {
		return flagEventPropagation;
	}
	
	@Override
	public void addMapObserver(MapObserver observer) {
		if(flagEventPropagation) {
			toAdd.add(observer);
		} else {
			observers.add(observer);
		}
	}

	@Override
	public void removeMapObserver(MapObserver observer) {
		if(flagEventPropagation) {
			toRemove.add(observer);
		} else {
			observers.remove(observer);
		}
	}

	private void endPropagation() {
		observers.removeAll(toRemove);
		observers.addAll(toAdd);
		toRemove.clear();
		toAdd.clear();
		flagEventPropagation = false;
	}

	@Override
	public <K, V> void onPut(String mapName, Map<K, V> changes) {
		flagEventPropagation = true;
		for(MapObserver obs : observers) {
			obs.onPut(mapName, changes);
		}
		endPropagation();
	}

	@Override
	public <K> void onRemove(String mapName, K key) {
		flagEventPropagation = true;
		for(MapObserver obs : observers) {
			obs.onRemove(mapName, key);
		}
		endPropagation();
	}

	@Override
	public void onClear(String mapName) {
		flagEventPropagation = true;
		for(MapObserver obs : observers) {
			obs.onClear(mapName);
		}
		endPropagation();
	}
}
