package com.github.kreaturesfw.core.legacy.asml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.kreaturesfw.core.error.InvokeException;
import com.github.kreaturesfw.core.reflection.Context;
import com.github.kreaturesfw.core.reflection.ContextProvider;

/**
 * Abstract base class for an ASML command. It provides some helper methods
 * to access variables in the current context, it implements the error handling and
 * the public execute method and it provides an executeInternal method which
 * has to be overridden by sub classes to define the execution behavior of the
 * ASML command.
 * 
 * @author Tim Janus
 */
public abstract class ASMLCommand implements ScriptCommand, ContextProvider {

	/** reference to the logback logger instance */
	private static Logger LOG = LoggerFactory.getLogger(ASMLCommand.class);
	
	/** the actual context used to execute the ASML command */
	private Context context;
	
	/** An exception showing the last error which occurred during ASML execution */
	private InvokeException lastError;
	
	@Override
	public boolean execute(Context context) {
		LOG.trace("start execution of: '{}'", this.getClass().getSimpleName());
		lastError = null;
		setContext(context);
		try {
			executeInternal();
		} catch (InvokeException e) {
			lastError = e;
			LOG.warn("An error occurred during ASML execution: {}", e.getMessage());
			//e.printStackTrace();
			return false;
		}
		return true;
	}
	
	@Override
	public InvokeException getLastError() {
		return lastError;
	}
	
	@Override
	public Context getContext() {
		return this.context;
	}
	
	/**
	 * Sets the context used for ASML execution. Sub classes might override this method
	 * if they have attributes which also need a context.
	 * @param context	The new context used for ASML execution.
	 */
	protected void setContext(Context context) {
		this.context = context;
	}
	
	/**
	 * The executeInternal method contains the execution behavior of the ASML command and
	 * has to be overridden by sub classes.
	 * @throws InvokeException
	 */
	protected abstract void executeInternal() throws InvokeException;
	
	/**
	 * Helper method: Returns the parameter value given by name
	 * @param name the name of the parameter.
	 * @return An instance of type T representing the parameter.
	 * @throws InvokeException 	Is thrown if the parameter with the given name can't be found or if the parameter can't be cast to T.
	 */
	protected <T> T getParameter(String name) throws InvokeException {
		if(name.startsWith("$"))
			name = name.substring(1);
		Object obj = context.get(name);
		if(obj == null) {
			throw InvokeException.parameterFailure(name, context);
		}
		
		// This throws a class cast exception if something went wrong
		try {
			@SuppressWarnings("unchecked")
			T reval = (T)obj;
			return reval;
		} catch(ClassCastException exec) {
			throw InvokeException.typeMismatch(name, obj.getClass(), context);
		}
	}
	
	protected <T> void setParameter(String name, T value) {
		context.set(name, value);
	}
	
	/**
	 * Sets the parameter for the return value.
	 * @param outName the name of the parameter
	 * @param out the value of the parameter.
	 * @throws InvokeException
	 */
	protected void setReturnValueIdentifier(String outName, Object out) throws InvokeException{
		if(outName == null)
			throw new IllegalArgumentException("'outName' must not be null.");
		
		if(outName.startsWith("$"))
			outName = outName.substring(1);
		
		Context in = getParameter("in");
		in.set(outName, out);
	}
}
