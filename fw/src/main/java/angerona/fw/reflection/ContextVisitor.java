package angerona.fw.reflection;

import angerona.fw.Agent;
import angerona.fw.error.InvokeException;
import angerona.fw.serialize.Statement;

/**
 * A ContextVisitor represents a method/operation which can be defined as
 * xml element. This is the base class for such visitors.
 * The visitor must be invoked by a context which defines the parameters used 
 * by the method/operation.
 * This base class defines some helpful methods for subclasses.
 * @author Tim Janus
 */
public abstract class ContextVisitor {

	/** the actual used context */
	protected Context context;
	
	/**
	 * Runs the ContextVisitor operations using the statements data structure received from a xml file
	 * and the given context.
	 * @param statement	data structure with parameter names and return value names.
	 * @param c reference to the context used for performing this method/operation.
	 * @throws InvokeException
	 */
	public void run(Statement statement, Context c) throws InvokeException {
		context = c;
		runImpl(statement);
	}

	/**
	 * method defining the implementation of the operation/method of this ContextVisitor.
	 * Subclasses must implement this
	 * @param statement	data strucutre with parameter names etc.
	 * @throws InvokeException
	 */
	protected abstract void runImpl(Statement statement) throws InvokeException;
	
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
			throw InvokeException.createParameterException(name, context);
		}
		
		// This throws a class cast exception if something went wrong
		try {
			@SuppressWarnings("unchecked")
			T reval = (T)obj;
			return reval;
		} catch(ClassCastException exec) {
			throw new InvokeException("Cant cast Parameter " + name + " to correct type. Actual: " + obj.getClass().getName(), context);
		}
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
