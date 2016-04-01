package com.github.kreatures.core.util;



/**
 * Base class for data models implementing the Model interface and
 * providing protected method to perform property changes for simple
 * properties and maps.
 * Internally the PropertyObservableSupport class is used as implementation for
 * simple property changes and MapObservableSupport for map content changes.
 * 
 * @author Tim Janus
 */
public class ModelAdapter implements Model {

	/** implementation of the property observable event mechanism */
    private PropertyObservableSupport propSupport = new PropertyObservableSupport();
    
    /** implementation of the ObservableMap event mechanism */
    private MultiMapObservable mapSupport = new MultiMapObservable();

	@Override
	public void addPropertyObserver(PropertyObserver observer) {
		propSupport.addPropertyObserver(observer);
	}

	@Override
	public void removePropertyObserver(PropertyObserver observer) {
		propSupport.removePropertyObserver(observer);
	}
	
	/**
	 * Call in this manner: this.property = changeProperty(name, this.property, newProperty). 
	 * It invokes the simple property event system to determine if the property can be changed
	 * @param propertyName
	 * @param oldValue
	 * @param newValue
	 * @return
	 */
	protected <T> T changeProperty(String propertyName, T oldValue, T newValue) {
		return propSupport.changeProperty(propertyName, oldValue, newValue);
	}

	@Override
	public void addMapObserver(MapObserver observer) {
		mapSupport.addMapObserver(observer);
	}

	@Override
	public void removeMapObserver(MapObserver observer) {
		mapSupport.removeMapObserver(observer);
	}
	
	protected void registerMap(ObservableMap<?, ?> map) {
		mapSupport.registerMap(map);
	}
	
	protected void unregisterMap(ObservableMap<?, ?> map) {
		mapSupport.unregisterMap(map);
	}
}
