package angerona.fw.reflection;

/**
 * Classes implementing this interface provide a script context of itself.
 * This means they can expose itself to the ASML script language.
 * 
 * @author Tim Janus
 */
public interface ContextProvider {
	/** @return A context object representing the attributes of this instance. */
	public Context getContext();
}
