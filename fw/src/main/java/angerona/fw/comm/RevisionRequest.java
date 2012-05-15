package angerona.fw.comm;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import net.sf.tweety.logics.firstorderlogic.syntax.FolFormula;

/**
 * Implementation of the speech act "RevisonRequest"
 * Used by a sender agent to reqeust the receiver agent to revise its 
 * beliefbase with the given sentence.
 * @author Tim Janus
 */
public class RevisionRequest extends SpeechAct
{
	/** the formula for revision */
	private Set<FolFormula> sentences = new HashSet<FolFormula>();
	
	public RevisionRequest(String sender, String receiver, FolFormula sentence) {
		super(sender, receiver);
		this.sentences.add(sentence);
	}	
	
	public RevisionRequest(String sender, String receiver, Set<FolFormula> sentences) {
		super(sender, receiver);
		this.sentences.addAll(sentences);
	}
	
	/** @return the formula for revision */
	public Set<FolFormula> getSentences() {
		return Collections.unmodifiableSet(sentences);
	}
	
	@Override 
	public String toString() {
		return "< " + this.getSenderId() + " revision-request " + this.getReceiverId() + " " + sentences + " >";
	}
}
