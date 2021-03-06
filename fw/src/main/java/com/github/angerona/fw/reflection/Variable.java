package com.github.angerona.fw.reflection;

import com.github.angerona.fw.error.InvokeException;

/**
 * The variable interface provides the ability to get an instance of a specific type T.
 * A variable is a wrapper for the type T. It either uses an instance of T and returns
 * it immediately or it uses an identifier like '$world' for the Variable of type T and
 * uses the provided Context to find the runtime variable.
 * @author Tim Janus
 *
 * @param <T>	The type which is wrapped by the Variable interface.
 */
public interface Variable<T> {
	/**
	 * returns the instance of type T wrapped by the Variable. 
	 * @param c		The Context of the variable.
	 * @return		An instance of type T representing the variable.
	 * @throws InvokeException
	 * @see angerona.fw.reflection.Context
	 */
	T getInstance(Context c) throws InvokeException;
}
