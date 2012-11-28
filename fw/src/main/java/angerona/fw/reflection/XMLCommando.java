package angerona.fw.reflection;

import angerona.fw.Agent;
import angerona.fw.error.InvokeException;

public class XMLCommando implements Commando {

	private Context context;
	
	@Override
	public void execute(Context context) {
		this.context = context;
	}
	
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
