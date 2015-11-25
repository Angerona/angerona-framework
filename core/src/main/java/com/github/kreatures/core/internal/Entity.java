package com.github.kreatures.core.internal;

import java.util.List;

/**
 * Every object in the KReatures environment is an entity. 
 * An Agent is an entity. A belief base is an entity. The plan
 * component is an entity.
 * 
 * Every entity has an unique id in the KReatures Framework, it might
 * hold the id of the parent entity (the agent owning a belief base is
 * the parent of the belief base), if the parent id is null there is no
 * parent (agents have no parent).
 * And it holds a list of ids of all its children entities. Agents for example
 * have a lot of children (the world and view belief bases and every data 
 * component).
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
