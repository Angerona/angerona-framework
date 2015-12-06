package com.github.kreaturesfw.core.error;

/**
 * This exception is thrown if something went wrong by instantiating a class which shall be
 * provided by the plug-in architecture.
 * For example the class name of the subgoal generation operator is wrong so no operator can
 * be instantiate and this exception is thrown.
 * @author Tim Janus
 */
public class AgentInstantiationException extends AngeronaException {

	public AgentInstantiationException(String message) {
		super(message);
	}

	/**
	 * kill warning
	 */
	private static final long serialVersionUID = -1182341481356815328L;

}
