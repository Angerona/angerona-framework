package angerona.fw;

import angerona.fw.logic.ViolatesResult;


/**
 * interface for perceptions.
 * @author Tim Janus
 */
public interface Perception extends AngeronaAtom {
	/** @return the id (unique name) of the receiver of the id */
	String getReceiverId();
	
	/** @return true if this instance is an action, false otherwise*/
	boolean isAction();
	
	/** sets the violates result this method must only be called by Violate Operators */
	void setViolates(ViolatesResult res);
}
