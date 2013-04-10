package angerona.fw.util;


/**
 * Base class for data models implementing the Model interface and
 * providing protected method to perform property change.
 * Internally the PropertyObservableSupport class is used as implementation and
 * therefore the implementation is not thread safe but new observers can be registered
 * during the event propagation.
 * 
 * @author Tim Janus
 */
public class ModelAdapter implements Model {

    private PropertyObservableSupport support = new PropertyObservableSupport();

	@Override
	public void addPropertyObserver(PropertyObserver observer) {
		support.addPropertyObserver(observer);
	}

	@Override
	public void removePropertyObserver(PropertyObserver observer) {
		support.removePropertyObserver(observer);
	}
	
	protected <T> T changeProperty(String propertyName, T oldValue, T newValue) {
		return support.changeProperty(propertyName, oldValue, newValue);
	}
}
