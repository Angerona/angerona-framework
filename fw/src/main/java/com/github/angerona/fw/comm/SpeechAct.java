package com.github.angerona.fw.comm;

import com.github.angerona.fw.Action;
import com.github.angerona.fw.Agent;

/**
 * Base class for every SpeechAct implemented by the Angerona framework.
 * 
 * To support meta-inferences the speech act has to implement a getType()
 * method that marks a speech act instance either as requesting or as 
 * informative, as proposed by Krümpelmann in mates2013.
 * 
 * @author Tim Janus
 */
public abstract class SpeechAct extends Action {

	/**
	 * An enum representing the type of speech act, that is either requesting (query, justify)
	 * or informative (inform, answer, justification).
	 * 
	 * @author Tim Janus
	 */
	public enum SpeechActType {
		SAT_INFORMATIVE,
		SAT_REQUESTING
	}
	
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
	
	/**
	 * 
	 * @return
	 */
	public abstract SpeechActType getType();
}
