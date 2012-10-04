package angerona.fw.comm;

import angerona.fw.Action;
import angerona.fw.Agent;

/**
 * Base class for every SpeechAct implemented by the Angerona framework.
 * @author Tim Janus
 */
public abstract class SpeechAct extends Action {

	/** Ctor used by deserilization */
	public SpeechAct(String sender, String receiver) {
		super(sender, receiver);
	}
	
	/**
	 * Ctor: creates a speech act with the given sender and receiver.
	 * @param sender	reference to the sending agent.
	 * @param receiver	unique name of the receiver agent.
	 */
	public SpeechAct(Agent sender, String receiver) {
		super(sender, receiver);
	}
}
