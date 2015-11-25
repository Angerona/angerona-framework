package com.github.kreatures.core.util;

/**
 * Classes implementing this interface allow other instances to become an
 * observer of their properties.
 * @author Tim Janus
 *
 */
public interface PropertyObservable {
	void addPropertyObserver(PropertyObserver observer);
	
	void removePropertyObserver(PropertyObserver observer);
}
