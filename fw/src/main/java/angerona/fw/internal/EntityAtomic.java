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
	/**
	 * Makes a copy of atomic entity. An atomic entitys copy duplicates
	 * every data defined in subclasses.
	 * The id and parent id is the same.  
	 * @return	A deep copy of this object
	 */
	public Object clone();
	
	/**
	 * Defines how often the atomic entity was cloned. Is it a copy (1),
	 * a copy of a copy (2), or even the copy of a copy of a copy (3).
	 * @return	an integer representing the copy depth.
	 */
	public int getCopyDepth();
}
