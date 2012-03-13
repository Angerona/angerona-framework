package angerona.fw.comm;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.Formula;

/**
 * Implementation of the speech act "RevsionResult". An agent A answers
 * the RevisionResult to another agent if the other agent has send a
 * RevisionRequest. The result contains the original set of formula which
 * were the request and also contains a set of formulas representing the
 * result of the revision (this set is a subset of the original set)
 * @author Tim Janus
 */
public class RevisionResult extends SpeechAct {

	private Set<Formula> regarding = new HashSet<Formula>();
	
	private Set<Formula> result = new HashSet<Formula>();
	
	public RevisionResult(String sender, String receiver,
			Set<Formula> regarding) {
		super(sender, receiver);
		this.regarding.addAll(regarding);
	}
	
	
	public RevisionResult(String sender, String receiver, 
			Set<Formula> regarding, Set<Formula> result) {
		super(sender, receiver);
		this.regarding.addAll(regarding);
		this.result.addAll(result);
	}

	public Set<Formula> getRegarding() {
		return Collections.unmodifiableSet(regarding);
	}
	
	public Set<Formula> getResult() {
		return Collections.unmodifiableSet(result);
	}
	
	@Override
	public String toString() {
		return "< " + getSenderId() + " revision-result " + getReceiverId() + " " + regarding + " " + result + " >";
	}
}
