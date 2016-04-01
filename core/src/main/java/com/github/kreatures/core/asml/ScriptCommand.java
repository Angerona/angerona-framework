package com.github.kreatures.core.asml;

import com.github.kreatures.core.error.InvokeException;
import com.github.kreatures.core.reflection.Context;

/**
 * This interface provides methods which a script command has to implement.
 * 
 * @author Tim Janus
 */
public interface ScriptCommand {
	
	/**
	 * Executes the script command using the given context.
	 * @param context	The context which is used during script 
	 * 					command execution.
	 * @return	true if the execution was successful, false if an error occurred.
	 */
	boolean execute(Context context);
	
	/**
	 * Informs the script command executor about the last error which occurred during
	 * the script execution.
	 * @return	An InvokeException representing the last error which occurred during
	 * 			the script execution.
	 */
	InvokeException getLastError();
}
