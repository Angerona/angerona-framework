package com.github.kreatures.core.util;

import java.util.HashSet;
import java.util.Set;

/**
 * Implements all features a class needs which wants to implement the PropertyObservable
 * interface and shall be used by PropertyObserverable implementations. 
 * It solves the problem that a PropertyChange event might add another observer to the 
 * PropertyObservable by buffering the add/remove operations during event propagation and 
 * performing them after the event propagation is finished. That means two things, first the newly 
 * added Observer does not receive the event but the next event and secondly the removed observer 
 * either was informed about the current event before its remove operation was buffered or it
 * gets informed about the event after the remove operation was buffered. In both cases the current
 * event is the last event processed by the removed Observer.
 * 
 * The implementation is not thread safe yet.
 * 
 * @author Tim Janus
 * @todo make thread safe
 */
public final class PropertyObservableSupport implements PropertyObservable {
	/** flag indicating if the event propagating is currently running */
	private boolean flagEventPropagating = false;
	
	/** the set contains the registered observers */
	private Set<PropertyObserver> observers = new HashSet<>();
	
	/** contains the buffered observers which shall be removed */
	private Set<PropertyObserver> toRemove = new HashSet<>();
	
	/** contains the buffered observers which shall be added */
	private Set<PropertyObserver> toAdd = new HashSet<>();
	
	@Override
	public void addPropertyObserver(PropertyObserver observer) {
		if(!flagEventPropagating) {
			observers.add(observer);
		} else {
			toAdd.add(observer);
		}
	}

	@Override
	public void removePropertyObserver(PropertyObserver observer) {
		if(!flagEventPropagating) {
			observers.remove(observer);
		} else {
			toRemove.add(observer);
		}
	}
	
	/**
	 * Changes a property using the underlying event mechanism, to understand
	 * the event mechanism see PropertyObserver interface. 
	 * @param propertyName	The name for the property used during event propagation
	 * @param oldValue		The old value of the property
	 * @param newValue		The new value of the property
	 * @return				The real value of the property, this might be oldValue,
	 * 						newValue or another value respond by the event system.
	 */
	public <T> T changeProperty(String propertyName, T oldValue, T newValue) {
		
		flagEventPropagating = true;
		// Propagate for check if this change is allowed
		if(!allowPropertyChange(propertyName, oldValue, newValue)) {
			processObserverChanges();
			return oldValue;
		}
		// Propagate for transformations of the property change
		newValue = transformPropertyChange(propertyName, oldValue, newValue);
		if(!allowPropertyChange(propertyName, oldValue, newValue)) {
			processObserverChanges();
			return oldValue;
		}
		// propagate the change of the property (informing observers)
		propertyChange(propertyName, oldValue, newValue);
		
		// update the registered observers:
		processObserverChanges();
		
		return newValue;
	}

	/** Add/Remove the observers which were added/removed during event propagation to the set of observers. */
	private void processObserverChanges() {
		observers.removeAll(toRemove);
		observers.addAll(toAdd);
		toRemove.clear();
		toAdd.clear();
		flagEventPropagating = false;
	}
	
	/**
	 * Propagates the allowPropertyChange event to every observer
	 * @param propertyName
	 * @param oldValue
	 * @param newValue
	 * @return	false if one PropertyObserver forbids the change, true otherwise.
	 */
	private <T> boolean allowPropertyChange(String propertyName, T oldValue,
			T newValue) {
		for(PropertyObserver obs : observers) {
			if(!obs.allowPropertyChange(propertyName, oldValue, newValue)) {
				return false;
			}
		}
		return true;
	}

	
	/**
	 * Propagates the TransformPropertyChange event to every observer
	 * @param propertyName
	 * @param oldValue
	 * @param newValue
	 * @return 	Either the parameter newValue or the first non null result of
	 * 			the PropertyObserver transformPropertyChange method. First means
	 * 			that those PropertyObservers are higher prioritzed which are at the
	 * 			front of the collection.
	 */
	private <T> T transformPropertyChange(String propertyName, T oldValue, T newValue) {
		for(PropertyObserver obs : observers) {
			T result = obs.transformPropertyChange(propertyName, oldValue, newValue);
			if(result != null)
				return result;
		}
		return newValue;
	}

	
	/**
	 * Propagates the PropertyChange event to every observer
	 * @param propertyName
	 * @param oldValue
	 * @param newValue
	 */
	private <T> void propertyChange(String propertyName, T oldValue,
			T newValue) {
		for(PropertyObserver obs : observers) {
			obs.propertyChange(propertyName, oldValue, newValue);
		}
	}
}
