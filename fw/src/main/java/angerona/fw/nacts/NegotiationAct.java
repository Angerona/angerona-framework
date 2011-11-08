package angerona.fw.nacts;

import angerona.fw.Action;

/**
 * Base class for every NegotiationAct.
 * @author Tim Janus
 */
public abstract class NegotiationAct extends Action {

	/**
	 * Ctor: creates a negotiation act with the given sender and receiver.
	 * @param sender	unique name of the sending agent.
	 * @param receiver	unique name of the receiver agent.
	 */
	public NegotiationAct(String sender, String receiver) {
		super(sender, receiver);
	}
}
