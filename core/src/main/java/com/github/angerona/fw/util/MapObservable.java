package com.github.angerona.fw.util;

/**
 * Classes implementing this interface allow observer to monitor maps.
 * 
 * @author Tim Janus
 */
public interface MapObservable {
	void addMapObserver(MapObserver observer);
	
	void removeMapObserver(MapObserver observer);
}
