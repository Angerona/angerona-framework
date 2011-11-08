package angerona.fw.error;

/**
 * This exception is thrown if something went wrong by instantiating a new agent.
 * As an example: The class name of the Filter Operator was wrong so no filter operator could
 * be instantiate.
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
