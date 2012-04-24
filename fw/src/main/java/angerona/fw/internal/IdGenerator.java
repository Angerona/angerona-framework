package angerona.fw.internal;

import java.util.HashMap;
import java.util.Map;


/**
 * Used for generating unique ids for the Angerona Entity
 * hierarchy.
 * @see Entity
 * @author Tim Janus
 */
public class IdGenerator {
	/** the start id of the counter */
	private static long nextId = 1;
	
	/** a map of ids to entity references */
	private static Map<Long, Entity> entityMap = new HashMap<Long, Entity>();
	
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
}
