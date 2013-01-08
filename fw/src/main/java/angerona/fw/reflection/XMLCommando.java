package angerona.fw.reflection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import angerona.fw.Agent;
import angerona.fw.error.InvokeException;

public abstract class XMLCommando implements Commando, ContextProvider {

	/** reference to the logback logger instance */
	private static Logger LOG = LoggerFactory.getLogger(XMLCommando.class);
	
	private Context context;
	
	private InvokeException lastError;
	
	@Override
	public boolean execute(Context context) {
		lastError = null;
		this.context = context;
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
	
	protected void setContext(Context context) {
		this.context = context;
	}
	
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
			return;
		
		if(outName.startsWith("$"))
			outName = outName.substring(1);
		
		Context in = getParameter("in");
		in.set(outName, out);
	}
	
	/**
	 * @return reference to the agent represent by the parameter self.
	 * @throws InvokeException
	 */
	protected Agent getSelf() throws InvokeException {
		return this.getParameter("self");
	}
}
