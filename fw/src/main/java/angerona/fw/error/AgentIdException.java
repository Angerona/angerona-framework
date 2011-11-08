package angerona.fw.error;

/**
 * This exception is thrown if something went wrong during id assignment.
 * The id strings must be unique in the environment.
 * @author Tim Janus
 */
public class AgentIdException extends AngeronaException {

	/**
	 * kill warning
	 */
	private static final long serialVersionUID = 2985773242519919603L;
	
	public AgentIdException(String message) {
		super(message);
	}
}
