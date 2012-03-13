package angerona.fw.comm;

import net.sf.tweety.Formula;

/**
 * Implementation of the SpeechAct "Why". An agent A asks another agent
 * for the reason of a sentence.
 * @author Tim Janus
 */
public class Why extends SpeechAct {

	private Formula sentence;
	
	public Why(String sender, String receiver, Formula sentence) {
		super(sender, receiver);
		this.sentence = sentence;
	}

	public Formula getSentence() {
		return sentence;
	}
	
	@Override
	public String toString() {
		return "< " +getSenderId() + " why " + getReceiverId() + " " + sentence + " >";
	}
}
