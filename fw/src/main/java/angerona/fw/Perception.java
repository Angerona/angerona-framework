package angerona.fw;


/**
 * interface for perceptions.
 * @author Tim Janus
 */
public interface Perception {
	/** @return the id (unique name) of the receiver of the id */
	String getReceiverId();
	
	/** @return true if this instance is an action, false otherwise*/
	boolean isAction();
}
