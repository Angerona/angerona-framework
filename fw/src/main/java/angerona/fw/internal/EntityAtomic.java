package angerona.fw.internal;

/**
 * Marks an entity as atomic. This means that this entity
 * is a leaf in the entity hierarchy. Children are types 
 * like beliefbases, desires, intentions. Parents are Agents 
 * (containing belief bases and so on).
 * 
 * @author Tim Janus
 */
public interface EntityAtomic extends Entity, Cloneable {
	public Object clone();
}
