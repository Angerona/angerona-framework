package angerona.fw.internal;

import java.util.HashMap;
import java.util.Map;


/**
 * Static methods are used for generating unique ids for the Angerona Entity
 * hierarchy.
 * 
 * Instances of this object can be used for generating unique ids for other objects
 * like desire arguments (queryProcessing(1), ..., queryProcessing(n))
 * 
 * BEWARE: using the correct instance of this class at the right place to ensure that
 * the id generation is unique for the needed id space.
 * 
 * BEWARE: the id generation will be reseted after Angerona exits. This means when restarting
 * the framework the id counts from the beginning.
 * 
 * @see Entity
 * @author Tim Janus
 */
public class IdGenerator {
	/** the start id of the counter */
	private static long nextId = 1;
	
	/** a map of ids to entity references */
	private static Map<Long, Entity> entityMap = new HashMap<Long, Entity>();
	
	private long next = 1;
	
	/**
	 * @param	ent	reference to the entity which wants the new id;
	 * @return 	the next free unique id.
	 */
	public static long generate(Entity ent) {
		long reval = nextId;
		nextId += 1;
		entityMap.put(reval, ent);
		return reval;
	}
	
	public static Entity getEntityWithId(Long id) {
		if(entityMap.containsKey(id)) {
			return entityMap.get(id);
		} else {
			return null;
		}
	}
	
	/** generator */
	public IdGenerator() {}
	
	/** @return the next unique id */
	public Long getNextId() {
		return next++;
	}
}
