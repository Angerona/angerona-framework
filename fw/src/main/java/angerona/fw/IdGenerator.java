package angerona.fw;

/**
 * Used for generating unique ids for the Angerona Report-Attachment
 * hierarchy.
 * @author Tim Janus
 */
public class IdGenerator {
	private static long nextId = 1;
	
	/**
	 * @return the next free unique id.
	 */
	public static long generate() {
		return nextId++;
	}
}
