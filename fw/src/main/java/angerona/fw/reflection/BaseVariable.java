package angerona.fw.reflection;

import angerona.fw.error.AngeronaException;
import angerona.fw.error.InvokeException;

/**
 * A BaseVariable is an abstract implementation of Variable. 
 * It provides the general method to find a variable instance in the
 * runtime context. It throws exceptions on errors like the variable
 * is not registered in the context or the type differs.
 * @author Tim Janus
 *
 * @param <T>	The type of the instanced wrapped by the Variable.
 */
public abstract class BaseVariable<T> implements Variable<T> {
	/** A string containing either the variable name or the string representation of the instance */
	private String content;

	/** A reference to the instance wrapped by the Variable */
	private T instance;
	
	/** Default Ctor: Used by simple xml for deserialization. */
	public BaseVariable() {}
	
	/** Ctor used to create wrapper during code-time */
	public BaseVariable(T instance) {
		this.instance = instance;
	}
	
	/**
	 * the method used by simple xml framework to initialize the Variable. If
	 * the content is no variable-name starting with '$' the method tries to 
	 * generate the instance using the abstract createInstanceFromString 
	 * implementation which is implemented by subclasses.
	 * @param content				String containing either the variable name of
	 * 								the variable instance in a runtime context or
	 * 								the string representation which can be used 
	 * 								for creation.
	 * @throws AngeronaException
	 */
	public void setContent(String content) throws AngeronaException {
		if(instance != null || this.content != null) {
			throw new AngeronaException("setContent of BaseVariable was called " +
					"outside an simple xml transformer.");
		}
		
		this.content = content;
		if(!this.content.startsWith("$")) {
			instance = createInstanceFromString(content);
		}
	}
	
	@Override
	public T getInstance(Context c) {
		if(instance == null) {
			try {
				instance = createInstance(c);
			} catch(InvokeException ex) {
				throw new RuntimeException("Error during variable lookup: " + ex.getMessage());
			}
		}
		return instance;
	}
	
	/**
	 * Helper method: Creates an instance of the variable by searching the given
	 * runtime Context for the instance linked with the variable name.
	 * @param c		The runtime context.
	 * @return		A reference to the instance wrapped by this Variable.
	 * @throws InvokeException
	 */
	@SuppressWarnings("unchecked")	// we check by a try catch block...
	protected T createInstance(Context c) throws InvokeException {
		Object obj = c.get(content.substring(1));
		if(obj == null) {
			throw InvokeException.parameterFailure(content, c);
		}
		T reval = null;
		try {
			reval = (T) obj;
		} catch(ClassCastException cce) {
			throw InvokeException.typeMismatch(content, obj.getClass(), c);
		}
		return reval;
	}
	
	@Override
	public String toString() {
		// if content is no variable there is no context needed in the getInstance method
		// for that it is safe to give null here.
		return (content == null ? getInstance(null).toString() : content);
	}
	
	/**
	 * Subclasses have to implement this method to convert a string into 
	 * an instance of type T. The subclass can indicate errors by throwing 
	 * the Angerona Exception.
	 * @param content		The string acting as source for conversion.
	 * @return				An instance of type T which is also represent by the given string.
	 * @throws AngeronaException
	 */
	protected abstract T createInstanceFromString(String content) throws AngeronaException;
}
