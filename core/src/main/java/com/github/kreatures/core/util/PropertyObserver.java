package com.github.kreatures.core.util;

/**
 * A class implementing the PropertyObserver interface can react to property changes 
 * of its observed object in three ways.
 * First the most commonly used is that it gets informed about a change and reacts to
 * this change. Secondly a PropertyObserver can forbid a property change by returning
 * false in the allowPropertyChange() method and thirdly it can transform a property change
 * by returning an own version of the new value for the property. 
 * 
 * An example for the second way is a blacklist of names which cannot be used like 'root', 
 * 'admin' etc.
 * 
 * For an example of the third method imagine a long text which might contain offense
 * language, then you might have a blacklist of words and replaces those words with
 * speichal characters like #$%".
 * 
 * @author Tim Janus
 */
public interface PropertyObserver {
	/**
	 * Checks if the proposed property change is allowed.
	 * @param propertyName
	 * @param oldValue
	 * @param newValue
	 * @return	true if it is allowed, false otherwise.
	 */
	<T> boolean allowPropertyChange(String propertyName, T oldValue, T newValue);
	
	/**
	 * Transforms the property change by returning another object which shall be used
	 * as new value for the property.
	 * @param propertyName
	 * @param oldValue
	 * @param newValue
	 * @return
	 */
	<T> T transformPropertyChange(String propertyName, T oldValue, T newValue);
	
	
	/**
	 * Is called if the property will occur but before it really occurs.
	 * @param propertyName
	 * @param oldValue
	 * @param newValue
	 */
	<T> void propertyChange(String propertyName, T oldValue, T newValue);
}
