package angerona.fw.reflection;

/**
 * Interface for a Condition. A Context can be injected using a setter method
 * and every Condition implementation provides a evaluate method which checks
 * if the Condition is fulfilled.
 * 
 * @author Tim Janus
 */
public interface Condition {
	void setContext(Context context);
	
	boolean evaluate();
}
