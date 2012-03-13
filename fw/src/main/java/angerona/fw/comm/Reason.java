package angerona.fw.comm;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.Formula;

/**
 * Implementation of speech act "Reason". A agent tells another agent the reason for
 * a sentence. The reason of a sentence is a set of formula where the sender agent
 * believes that they make the receiver agent capable of deduct the regarding formula.
 * 
 * @author Tim Janus
 */
public class Reason extends SpeechAct {

	private Formula regarding;
	
	private Set<Formula> reason = new HashSet<Formula>();
	
	public Reason(String sender, String receiver, Formula regarding,
			Set<Formula> reason) {
		super(sender, receiver);
	}

	public Formula getRegarding() {
		return regarding;
	}
	
	public Set<Formula> getReason() {
		return Collections.unmodifiableSet(reason);
	}
	
	@Override
	public String toString() {
		return "< " + getSenderId() + " reason " + getReceiverId() + " " + regarding + " " + reason + " >";
	}
}
