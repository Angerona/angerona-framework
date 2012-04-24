package angerona.fw.internal;

import java.util.List;

/**
 * Every object in the Angerona environment is an entity. 
 * An Agent is an entity. A Beliefbase is an entity and so on.
 * 
 * @author Tim Janus
 */
public interface Entity {
	/** @return the id of the entity */
	Long getGUID();
	
	/** @return the id of the parent of this entity (might be null that means no parent) */
	Long getParent();
	
	/** @return a list of ids of all objects which are children of this instance. */
	List<Long> getChilds();
}
