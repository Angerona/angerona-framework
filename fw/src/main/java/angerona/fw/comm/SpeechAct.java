package angerona.fw.comm;

import angerona.fw.Action;

/**
 * Base class for every SpeechAct implemented by the Angerona framework.
 * @author Tim Janus
 */
public abstract class SpeechAct extends Action {

	/**
	 * Ctor: creates a speech act with the given sender and receiver.
	 * @param sender	unique name of the sending agent.
	 * @param receiver	unique name of the receiver agent.
	 */
	public SpeechAct(String sender, String receiver) {
		super(sender, receiver);
	}
}
