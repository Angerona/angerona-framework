package angerona.fw.reflection;

/**
 * Interface for all objects which provide a context of themself.
 * @author Tim Janus
 */
public interface ContextProvider {
	/** returns a Context object representing the attributes of this */
	public Context getContext();
}
